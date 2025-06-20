package com.example.desafiosenai.dtos.responses;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductResponseDto(
        Integer id,
        String name,
        String description,
        Integer stock,
        BigDecimal price,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt
) {
}
