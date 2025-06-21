package com.example.desafiosenai.dtos.responses;

import com.example.desafiosenai.entities.CouponType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CouponResponseDto(
        Integer id,
        String code,
        CouponType type,
        BigDecimal discountValue,
        Boolean oneShot,
        LocalDateTime validFrom,
        LocalDateTime validUntil,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt
) {
}
