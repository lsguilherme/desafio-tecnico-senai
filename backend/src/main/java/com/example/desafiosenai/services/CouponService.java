package com.example.desafiosenai.services;

import com.example.desafiosenai.dtos.requests.CouponRequestDto;
import com.example.desafiosenai.dtos.responses.CouponResponseDto;
import com.example.desafiosenai.entities.CouponEntity;
import com.example.desafiosenai.entities.CouponType;
import com.example.desafiosenai.exceptions.BadRequestException;
import com.example.desafiosenai.exceptions.ConflictException;
import com.example.desafiosenai.exceptions.ResourceNotFoundException;
import com.example.desafiosenai.repositories.CouponRepository;
import com.example.desafiosenai.utils.CodeNormalizer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class CouponService {
    private final CouponRepository couponRepository;
    private final CodeNormalizer codeNormalizer;
    private final ObjectMapper objectMapper;

    public CouponService(CouponRepository couponRepository, CodeNormalizer codeNormalizer, ObjectMapper objectMapper) {
        this.couponRepository = couponRepository;
        this.codeNormalizer = codeNormalizer;
        this.objectMapper = objectMapper;
    }

    public List<CouponResponseDto> findAllCoupons() {
        return couponRepository.findAll().stream().map(CouponEntity::toDto).toList();
    }

    @Transactional
    public CouponResponseDto createCoupon(CouponRequestDto couponRequestDto) {
        String normalizedCode = codeNormalizer.normalizedCode(couponRequestDto.code());

        boolean existsCode = couponRepository.existsByCode(normalizedCode);

        if (existsCode) {
            throw new ConflictException("O cupom já existe.");
        }

        validateCoupon(couponRequestDto);

        CouponEntity coupon = new CouponEntity();
        coupon.setCode(normalizedCode);
        coupon.setType(couponRequestDto.type());
        coupon.setDiscountValue(couponRequestDto.discountValue());
        coupon.setOneShot(couponRequestDto.oneShot());
        coupon.setMaxUses(couponRequestDto.maxUses());
        coupon.setValidUntil(couponRequestDto.validUntil());
        coupon.setValidFrom(couponRequestDto.validFrom());

        var savedCoupon = couponRepository.save(coupon);
        return savedCoupon.toDto();
    }


    public CouponResponseDto findCouponByCode(String code) {
        String normalized = codeNormalizer.normalizedCode(code);
        return couponRepository.findByCode(normalized)
                .map(CouponEntity::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Cupom não encontrado"));
    }

    @Transactional
    public void deleteCouponByCode(String code) {
        CouponEntity coupon = couponRepository.findByCodeAndDeletedAtIsNull(code).orElseThrow(() ->
                new ResourceNotFoundException("Produto não encontrado."));

        coupon.softDelete();

        couponRepository.save(coupon);
    }

    private void validateCoupon(CouponRequestDto couponRequestDto) {
        validateDates(couponRequestDto.validFrom(), couponRequestDto.validUntil());
        validateCouponUses(couponRequestDto.oneShot(), couponRequestDto.maxUses());
        validateValueByType(couponRequestDto.type(), couponRequestDto.discountValue());
    }

    private void validateCouponEntity(CouponEntity couponEntity) {
        validateDates(couponEntity.getValidFrom(), couponEntity.getValidUntil());
        validateCouponUses(couponEntity.getOneShot(), couponEntity.getMaxUses());
        validateValueByType(couponEntity.getType(), couponEntity.getDiscountValue());
    }

    private void validateDates(LocalDateTime validFrom, LocalDateTime validUntil) {
        if (validFrom.isAfter(validUntil)) {
            throw new BadRequestException("O campo 'valid_until' deve ser posterior ao 'valid_from'.");
        }

        long yearsBetween = ChronoUnit.YEARS.between(validFrom, validUntil);
        if (yearsBetween > 5) {
            throw new BadRequestException("A diferença entre 'valid_from' e 'valid_until' não pode exceder 5 anos.");
        }
    }

    private void validateCouponUses(Boolean oneShot, Integer maxUses) {
        if (!oneShot) {
            if (maxUses == null || maxUses < 1) {
                throw new BadRequestException("Quando 'oneShot' for false 'max_uses' é obrigatório.");
            }
        } else {
            if (maxUses != null) {
                throw new BadRequestException("Quando 'oneShot' for true o 'max_uses' deve ser null");
            }
        }
    }

    private void validateValueByType(CouponType type, BigDecimal value) {
        if (type == CouponType.PERCENT) {
            if (value.compareTo(BigDecimal.ONE) < 0 || value.compareTo(BigDecimal.valueOf(80)) > 0) {
                throw new BadRequestException("Cupom do tipo 'percent' deve ser entre 1 e 80 porcento.");
            }
        } else if (type == CouponType.FIXED) {
            if (value.compareTo(BigDecimal.ZERO) <= 0) {
                throw new BadRequestException("Cupom do tipo 'fixed' deve ser maior que 0.");
            }
        } else {
            throw new BadRequestException("Tipo de cupom inválido");
        }
    }

    @Transactional
    public CouponResponseDto updateCoupon(String code, JsonPatch patch) {
        String normalizedCode = codeNormalizer.normalizedCode(code);
        CouponEntity coupon = couponRepository.findByCode(normalizedCode)
                .orElseThrow(() -> new ResourceNotFoundException("Cupom não encontrado."));

        try {
            JsonNode couponNode = objectMapper.valueToTree(coupon);

            JsonNode patchedCouponNode = patch.apply(couponNode);

            CouponEntity tempPatchedCoupon = objectMapper.treeToValue(patchedCouponNode, CouponEntity.class);

            if (!coupon.getCode().equals(tempPatchedCoupon.getCode())) {
                String newNormalizedCode = codeNormalizer.normalizedCode(tempPatchedCoupon.getCode());
                if (couponRepository.existsByCode(newNormalizedCode)) {
                    throw new ConflictException("Já existe um cupom com o novo código fornecido.");
                }
                coupon.setCode(newNormalizedCode);
            }

            coupon.setCode(tempPatchedCoupon.getCode());
            coupon.setType(tempPatchedCoupon.getType());
            coupon.setDiscountValue(tempPatchedCoupon.getDiscountValue());
            coupon.setOneShot(tempPatchedCoupon.getOneShot());
            coupon.setMaxUses(tempPatchedCoupon.getMaxUses());
            coupon.setValidFrom(tempPatchedCoupon.getValidFrom());
            coupon.setValidUntil(tempPatchedCoupon.getValidUntil());

            validateCouponEntity(coupon);

            return couponRepository.save(coupon).toDto();

        } catch (JsonPatchException e) {
            throw new BadRequestException("Formato de patch inválido: " + e.getMessage());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao processar o patch do cupom: " + e.getMessage());
        }
    }
}