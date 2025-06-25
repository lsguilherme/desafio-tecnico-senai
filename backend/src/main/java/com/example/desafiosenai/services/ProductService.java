package com.example.desafiosenai.services;

import com.example.desafiosenai.dtos.requests.ProductRequestDto;
import com.example.desafiosenai.dtos.responses.PagedResponseDto;
import com.example.desafiosenai.dtos.responses.ProductResponseDto;
import com.example.desafiosenai.entities.CouponEntity;
import com.example.desafiosenai.entities.CouponType;
import com.example.desafiosenai.entities.ProductCouponApplicationEntity;
import com.example.desafiosenai.entities.ProductEntity;
import com.example.desafiosenai.exceptions.BadRequestException;
import com.example.desafiosenai.exceptions.ConflictException;
import com.example.desafiosenai.exceptions.ResourceNotFoundException;
import com.example.desafiosenai.exceptions.UnprocessableEntityException;
import com.example.desafiosenai.repositories.CouponRepository;
import com.example.desafiosenai.repositories.ProductRepository;
import com.example.desafiosenai.utils.CodeNormalizer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CouponRepository couponRepository;
    private final CodeNormalizer codeNormalizer;
    private final ObjectMapper objectMapper;

    public ProductService(ProductRepository productRepository, CouponRepository couponRepository, CodeNormalizer codeNormalizer, ObjectMapper objectMapper) {
        this.productRepository = productRepository;
        this.couponRepository = couponRepository;
        this.codeNormalizer = codeNormalizer;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public ProductResponseDto saveProduct(ProductRequestDto productRequestDto) {
        String normalizedName = productRequestDto.name().replaceAll("\\s+", " ").trim();

        if (productRepository.findByName(normalizedName).isPresent()) {
            throw new ConflictException("Já existe um produto com esse nome.");
        }
        ;

        ProductEntity product = new ProductEntity();
        product.setName(normalizedName);
        product.setDescription(productRequestDto.description());
        product.setStock(productRequestDto.stock());
        product.setPrice(productRequestDto.price());

        var savedProduct = productRepository.save(product);
        return savedProduct.toDto();

    }

    @Transactional
    public void deleteProductById(Integer id) {
        ProductEntity product = productRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(() ->
                new ResourceNotFoundException("Produto não encontrado."));

        product.softDelete();

        productRepository.save(product);
    }

    @Transactional
    public ProductResponseDto restoreProductById(Integer id) {
        ProductEntity product = productRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Produto não encontrado."));

        if (product.getDeletedAt() == null) {
            throw new BadRequestException("Produto já está ativo.");
        }

        product.restore();

        return productRepository.save(product).toDto();
    }

    public ProductResponseDto findProductById(Integer id) {
        return productRepository.findById(id).map(ProductEntity::toDto).orElseThrow(() ->
                new ResourceNotFoundException("Produto não encontrado."));
    }

    @Transactional
    public ProductResponseDto applyCouponToProduct(Integer id, String code) {
        ProductEntity product = productRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(() ->
                new ResourceNotFoundException("Produto não encontrado."));

        String normalizedCode = codeNormalizer.normalizedCode(code);

        CouponEntity coupon = couponRepository.findByCodeAndDeletedAtIsNull(normalizedCode).orElseThrow(() ->
                new ResourceNotFoundException("Cupom não encontrado ou inativo."));

        if (coupon.getValidFrom().isAfter(LocalDateTime.now())) {
            throw new BadRequestException("Cupom ainda não está valido.");
        }
        if (coupon.getValidUntil().isBefore(LocalDateTime.now())){
            throw new BadRequestException("Cupom expirado.");
        }

        if (coupon.getMaxUses() != null && coupon.getMaxUses() <= 0) {
            throw new ConflictException("Este cupom não possui mais usos disponíveis.");
        }

        boolean hasActiveCoupon = product.getProductCouponApplications().stream().anyMatch(pca -> pca.getRemovedAt() == null);

        if (hasActiveCoupon) {
            throw new ConflictException("Já existe um cupom ativo para este produto.");
        }

        BigDecimal finalPrice = product.getPrice();
        if (coupon.getType() == CouponType.PERCENT) {
            BigDecimal discountPercentage = coupon.getDiscountValue().divide(BigDecimal.valueOf(100), MathContext.DECIMAL128);
            finalPrice = product.getPrice().multiply(BigDecimal.ONE.subtract(discountPercentage));
        } else if (coupon.getType() == CouponType.FIXED) {
            finalPrice = product.getPrice().subtract(coupon.getDiscountValue());
        }

        if (finalPrice.compareTo(new BigDecimal("0.01")) < 0) {
            throw new UnprocessableEntityException("O desconto do cupom faz o preço final cair para menos de R$ 0,01.");
        }

        ProductCouponApplicationEntity productCoupon = new ProductCouponApplicationEntity();
        productCoupon.setProduct(product);
        productCoupon.setCoupon(coupon);
        productCoupon.setAppliedAt(LocalDateTime.now());
        product.getProductCouponApplications().add(productCoupon);

        if (coupon.getOneShot()) {
            coupon.softDelete();
            couponRepository.save(coupon);
        } else if (coupon.getMaxUses() != null) {
            coupon.setMaxUses(coupon.getMaxUses() - 1);
            couponRepository.save(coupon);
        }

        return productRepository.save(product).toDto();
    }

    @Transactional
    public void removeDiscountFromProduct(Integer id) {
        ProductEntity product = productRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(() ->
                new ResourceNotFoundException("Produto não encontrado."));
        Optional<ProductCouponApplicationEntity> activeCoupon = product.getProductCouponApplications().stream()
                .filter(pc -> pc.getRemovedAt() == null)
                .findFirst();

        if (activeCoupon.isEmpty()) {
            throw new BadRequestException("Nenhum desconto ativo para esse produto.");
        }

        ProductCouponApplicationEntity appliedCoupon = activeCoupon.get();
        appliedCoupon.setRemovedAt(LocalDateTime.now());

        productRepository.save(product);
    }

    @Transactional
    public ProductResponseDto applyPercentDiscount(Integer id, BigDecimal percent) {
        ProductEntity product = productRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(() ->
                new ResourceNotFoundException("Produto não encontrado."));

        if (percent.compareTo(BigDecimal.valueOf(1)) < 0 || percent.compareTo(BigDecimal.valueOf(80)) > 0) {
            throw new BadRequestException("O desconto percentual deve ser entre 1% e 80%.");
        }

        BigDecimal discountPercentage = percent.divide(BigDecimal.valueOf(100), MathContext.DECIMAL128);
        BigDecimal finalPrice = product.getPrice().multiply(BigDecimal.ONE.subtract(discountPercentage));

        if (finalPrice.compareTo(new BigDecimal("0.01")) < 0) {
            throw new UnprocessableEntityException("O desconto percentual faz o preço final cair para menos de R$ 0,01.");
        }

        String couponCode = codeNormalizer.normalizedCode("PERCENTUAL" + id);
        Optional<CouponEntity> existingCoupon = couponRepository.findByCode(couponCode);

        CouponEntity coupon;
        if (existingCoupon.isPresent()) {
            coupon = existingCoupon.get();

            if (coupon.getType() != CouponType.PERCENT) {
                throw new ConflictException("Já existe um cupom de outro tipo aplicado a este produto. Remova-o antes de aplicar um desconto percentual.");
            }

            if (coupon.getDeletedAt() != null) {
                coupon.restore();
            }

            coupon.setDiscountValue(percent);
            coupon.setValidUntil(LocalDateTime.now().plusYears(5));
        } else {
            coupon = new CouponEntity();
            coupon.setCode(couponCode);
            coupon.setType(CouponType.PERCENT);
            coupon.setDiscountValue(percent);
            coupon.setOneShot(false);
            coupon.setMaxUses(null);
            coupon.setValidFrom(LocalDateTime.now());
            coupon.setValidUntil(LocalDateTime.now().plusYears(5));
        }

        couponRepository.save(coupon);

        product.getProductCouponApplications().stream()
                .filter(pc -> pc.getRemovedAt() == null && !pc.getCoupon().equals(coupon))
                .forEach(pc -> pc.setRemovedAt(LocalDateTime.now()));

        boolean isCouponAlreadyApplied = product.getProductCouponApplications().stream()
                .anyMatch(pc -> pc.getCoupon().equals(coupon) && pc.getRemovedAt() == null);

        if (!isCouponAlreadyApplied) {
            ProductCouponApplicationEntity application = new ProductCouponApplicationEntity();
            application.setProduct(product);
            application.setCoupon(coupon);
            application.setAppliedAt(LocalDateTime.now());
            product.getProductCouponApplications().add(application);
        }

        return productRepository.save(product).toDto();
    }

    public PagedResponseDto<ProductResponseDto> findAllProducts(int page, int limit, String search, BigDecimal minPrice, BigDecimal maxPrice, Boolean hasDiscount, String sortBy, String sortOrder, Boolean includeDeleted, Boolean onlyOutOfStock) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortBy);
        Pageable pageable = PageRequest.of(page, limit, sort);

        Specification<ProductEntity> spec = Specification.allOf();

        if (search != null && !search.trim().isEmpty()) {
            String lowerSearch = search.toLowerCase();
            spec = spec.and(((root, query, criteriaBuilder) ->
                    criteriaBuilder.or(
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + lowerSearch + "%"))));
        }

        if (minPrice != null) {
            spec = spec.and(((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice)));
        }

        if (maxPrice != null) {
            spec = spec.and(((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice)));
        }

        if (Boolean.FALSE.equals(includeDeleted)) {
            spec = spec.and(((root, query, criteriaBuilder) ->
                    criteriaBuilder.isNull(root.get("deletedAt"))));
        }

        if (Boolean.TRUE.equals(onlyOutOfStock)) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("stock"), 0)
            );
        }

        if (hasDiscount != null) {
            spec = spec.and(((root, query, criteriaBuilder) -> {
                Join<ProductEntity, ProductCouponApplicationEntity> productJoin = root.join("productCouponApplications", JoinType.LEFT);
                Join<ProductCouponApplicationEntity, CouponEntity> couponJoin = productJoin.join("coupon", JoinType.LEFT);

                Predicate activeDiscount = criteriaBuilder.and(
                        criteriaBuilder.isNull(productJoin.get("removedAt")),
                        criteriaBuilder.isNull(couponJoin.get("deletedAt")),
                        criteriaBuilder.lessThanOrEqualTo(couponJoin.get("validFrom"), LocalDateTime.now()),
                        criteriaBuilder.greaterThanOrEqualTo(couponJoin.get("validUntil"), LocalDateTime.now())
                );

                if (Boolean.TRUE.equals(hasDiscount)) {
                    return activeDiscount;
                } else {
                    return criteriaBuilder.not(activeDiscount);
                }
            }));
        }

        Page<ProductEntity> productPage = productRepository.findAll(spec, pageable);

        List<ProductResponseDto> content = productPage.getContent().stream()
                .map(ProductEntity::toDto)
                .collect(Collectors.toList());

        return new PagedResponseDto<>(
                content, new PagedResponseDto.Meta(
                productPage.getNumber(),
                productPage.getSize(),
                productPage.getTotalPages(),
                productPage.getNumberOfElements()
        )
        );
    }

    @Transactional
    public ProductResponseDto updateProduct(Integer id, JsonPatch patch) {
        ProductEntity product = productRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(() ->
                new ResourceNotFoundException("Produto não encontrado."));

        try {
            JsonNode productNode = objectMapper.valueToTree(product);

            JsonNode patchedProductNode = patch.apply(productNode);

            ProductEntity tempPatchedProduct = objectMapper.treeToValue(patchedProductNode, ProductEntity.class);

            if (tempPatchedProduct.getPrice().compareTo(BigDecimal.valueOf(0.01)) < 0 || tempPatchedProduct.getPrice().compareTo(BigDecimal.valueOf(1000000.00)) > 0) {
                throw new BadRequestException("O preço deve ser entre 0.01 e 1.000.000,00.");
            }
            if (tempPatchedProduct.getStock() < 0 || tempPatchedProduct.getStock() > 999999) {
                throw new BadRequestException("O estoque deve ser entre 0 e 999999.");
            }
            String normalizedName = tempPatchedProduct.getName().replaceAll("\\s+", " ").trim();

            if (productRepository.findByName(normalizedName).isPresent() && !productRepository.findByName(normalizedName).get().getId().equals(id)) {
                throw new ConflictException("Já existe um produto com esse nome.");
            }

            product.setName(tempPatchedProduct.getName());
            product.setDescription(tempPatchedProduct.getDescription());
            product.setPrice(tempPatchedProduct.getPrice());
            product.setStock(tempPatchedProduct.getStock());

            product.getProductCouponApplications().clear();
            if (tempPatchedProduct.getProductCouponApplications() != null) {
                for (ProductCouponApplicationEntity app : tempPatchedProduct.getProductCouponApplications()) {
                    app.setProduct(product);
                    product.getProductCouponApplications().add(app);
                }
            }

            return productRepository.save(product).toDto();
        } catch (JsonPatchException e) {
            throw new BadRequestException("Formato de patch inválido: " + e.getMessage());
        } catch (IOException e){
            throw new RuntimeException("Erro ao processar patch: " + e.getMessage());
        }
    }
}
