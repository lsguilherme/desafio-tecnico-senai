package com.example.desafiosenai.services;

import com.example.desafiosenai.dtos.requests.ProductRequestDto;
import com.example.desafiosenai.dtos.responses.ProductResponseDto;
import com.example.desafiosenai.entities.CouponEntity;
import com.example.desafiosenai.entities.ProductCouponApplicationEntity;
import com.example.desafiosenai.entities.ProductEntity;
import com.example.desafiosenai.repositories.CouponRepository;
import com.example.desafiosenai.repositories.ProductRepository;
import com.example.desafiosenai.utils.CodeNormalizer;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CouponRepository couponRepository;
    private final CodeNormalizer codeNormalizer;

    public ProductService(ProductRepository productRepository, CouponRepository couponRepository, CodeNormalizer codeNormalizer) {
        this.productRepository = productRepository;
        this.couponRepository = couponRepository;
        this.codeNormalizer = codeNormalizer;
    }

    public ProductResponseDto saveProduct(ProductRequestDto productRequestDto) {
        String normalizedName = productRequestDto.name().replaceAll("\\s+", " ").trim();

        if (productRepository.findByName(normalizedName).isPresent()) {
            throw new IllegalArgumentException("Já existe um produto com esse nome.");
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

    public void deleteProductById(Integer id) {
        ProductEntity product = productRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(() -> new IllegalArgumentException("Produto não encontrado."));

        product.softDelete();

        productRepository.save(product);
    }

    public ProductResponseDto restoreProductById(Integer id) {
        ProductEntity product = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Produto não encontrado."));

        if (product.getDeletedAt() == null) {
            throw new IllegalArgumentException("Produto já está ativo.");
        }

        product.restore();

        return productRepository.save(product).toDto();
    }

    public ProductResponseDto findProductById(Integer id) {
        return productRepository.findById(id).map(ProductEntity::toDto).orElseThrow(() -> new IllegalArgumentException("Produto não encontrado."));
    }

    @Transactional
    public ProductResponseDto applyCouponToProduct(Integer id, String code) {
        ProductEntity product = productRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(() -> new IllegalArgumentException("Produto não encontrado."));

        String normalizedCode = codeNormalizer.normalizedCode(code);

        CouponEntity coupon = couponRepository.findByCodeAndDeletedAtIsNull(normalizedCode).orElseThrow(() -> new IllegalArgumentException("Cupom não encontrado ou inativo."));

        if (coupon.getValidFrom().isAfter(LocalDateTime.now()) || coupon.getValidUntil().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cupom fora da data de válidade.");
        }

        if (coupon.getMaxUses() != null && coupon.getMaxUses() <= 0) {
            throw new IllegalArgumentException("Este cupom não possui mais usos disponíveis.");
        }

        boolean hasActiveCoupon = product.getProductCouponApplications().stream().anyMatch(pca -> pca.getRemovedAt() == null);

        if (hasActiveCoupon) {
            throw new IllegalArgumentException("Já existe um cupom ativo para este produto.");
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
        ProductEntity product = productRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(() -> new IllegalArgumentException("Produto não encontrado."));
        Optional<ProductCouponApplicationEntity> activeCoupon = product.getProductCouponApplications().stream()
                .filter(pc -> pc.getRemovedAt() == null)
                .findFirst();

        if (activeCoupon.isEmpty()) {
            throw new IllegalArgumentException("Nenhum desconto ativo para esse produto.");
        }

        ProductCouponApplicationEntity appliedCoupon = activeCoupon.get();
        appliedCoupon.setRemovedAt(LocalDateTime.now());

        productRepository.save(product);
    }
}
