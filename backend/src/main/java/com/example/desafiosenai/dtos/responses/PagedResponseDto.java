package com.example.desafiosenai.dtos.responses;

import java.util.List;

public record PagedResponseDto<T>(
        List<T> data,
        Meta meta
) {
    public record Meta(
            int page,
            int limit,
            int totalPages,
            int totalItems
    ){}
}
