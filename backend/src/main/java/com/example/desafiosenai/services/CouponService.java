package com.example.desafiosenai.services;

import com.example.desafiosenai.dtos.requests.CouponRequestDto;
import com.example.desafiosenai.dtos.responses.CouponResponseDto;
import com.example.desafiosenai.entities.CouponEntity;
import com.example.desafiosenai.entities.CouponType;
import com.example.desafiosenai.repositories.CouponRepository;
import com.example.desafiosenai.utils.CodeNormalizer;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class CouponService {
    private final CouponRepository couponRepository;
    private final CodeNormalizer codeNormalizer;

    public CouponService(CouponRepository couponRepository, CodeNormalizer codeNormalizer) {
        this.couponRepository = couponRepository;
        this.codeNormalizer = codeNormalizer;
    }

    public List<CouponResponseDto> findAllCoupons() {
        return couponRepository.findAll().stream().map(CouponEntity::toDto).toList();
    }

    public CouponResponseDto createCoupon(CouponRequestDto couponRequestDto) {
        String normalizedCode = codeNormalizer.normalizedCode(couponRequestDto.code());

        boolean existsCode = couponRepository.existsByCode(normalizedCode);

        if (existsCode) {
            throw new IllegalArgumentException("O cupom já existe.");
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
                .orElseThrow(() -> new IllegalArgumentException("Cupom não encontrado"));
    }

    public void deleteCouponByCode(String code) {
        CouponEntity coupon = couponRepository.findByCodeAndDeletedAtIsNull(code).orElseThrow(() -> new IllegalArgumentException("Produto não encontrado."));

        coupon.softDelete();

        couponRepository.save(coupon);
    }

    private void validateCoupon(CouponRequestDto couponRequestDto) {
        validateDates(couponRequestDto.validFrom(), couponRequestDto.validUntil());
        validateCouponUses(couponRequestDto.oneShot(), couponRequestDto.maxUses());
        validateValueByType(couponRequestDto.type(), couponRequestDto.discountValue());
    }

    private void validateDates(LocalDateTime validFrom,  LocalDateTime validUntil) {
        if (validFrom.isAfter(validUntil)) {
            throw new IllegalArgumentException("O campo 'valid_until' deve ser posterior ao 'valid_from'.");
        }

        long yearsBetween = ChronoUnit.YEARS.between(validFrom, validUntil);
        if (yearsBetween > 5) {
            throw new IllegalArgumentException("A diferença entre 'valid_from' e 'valid_until' não pode exceder 5 anos.");
        }
    }

    private void validateCouponUses(Boolean oneShot, Integer maxUses) {
        if (!oneShot) {
            if (maxUses == null || maxUses < 1) {
                throw new IllegalArgumentException("Quando 'oneShot' for false 'max_uses' é obrigatório.");
            }
        } else {
            if (maxUses != null) {
                throw new IllegalArgumentException("Quando 'oneShot' for true o 'max_uses' deve ser null");
            }
        }
    }

    private void validateValueByType(CouponType type, BigDecimal value) {
        if (type == CouponType.PERCENT) {
            if (value.compareTo(BigDecimal.ONE) < 0 || value.compareTo(BigDecimal.valueOf(80)) > 0) {
                throw new IllegalArgumentException("Cupom do tipo 'percent' deve ser entre 1 e 80 porcento.");
            }
        } else if (type == CouponType.FIXED) {
            if(value.compareTo(BigDecimal.ZERO) <= 0){
                throw new IllegalArgumentException("Cupom do tipo 'fixed' deve ser maior que 0.");
            }
        } else {
            throw new IllegalArgumentException("Tipo de cupom inválido");
        }
    }
}