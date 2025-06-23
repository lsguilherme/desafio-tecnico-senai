package com.example.desafiosenai.dtos;

import com.example.desafiosenai.entities.CouponType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DiscountDto(
        CouponType type,
        BigDecimal value,
        LocalDateTime appliedAt
) {
}
