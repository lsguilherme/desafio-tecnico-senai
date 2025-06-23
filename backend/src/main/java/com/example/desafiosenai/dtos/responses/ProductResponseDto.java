package com.example.desafiosenai.dtos.responses;

import com.example.desafiosenai.dtos.DiscountDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductResponseDto(
        Integer id,
        String name,
        String description,
        Integer stock,
        Boolean isOutOfStock,
        BigDecimal price,
        BigDecimal finalPrice,
        DiscountDto discount,
        Boolean hasCouponApplied,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt
) {
}
