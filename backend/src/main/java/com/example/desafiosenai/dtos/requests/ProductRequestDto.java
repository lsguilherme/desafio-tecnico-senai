package com.example.desafiosenai.dtos.requests;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductRequestDto(
        @NotBlank(message = "O nome não pode estar em branco.")
        @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
        @Pattern(regexp = "^[a-zA-ZÀ-ÿ0-9\\s\\-_,.]+$", message = "O nome contém caracteres inválidos.")
        String name,

        @Size(max = 300, message = "A descrição deve ter até 300 caracteres.")
        String description,

        @NotNull(message = "O preço não pode ser nulo.")
        @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero.")
        @DecimalMax(value = "1000000.00", message = "O preço máximo é 1.000.000,00.")
        BigDecimal price,

        @Min(value = 0, message = "O estoque mínimo permitido é 0.")
        @Max(value = 999999, message = "O estoque máximo permitido é 999999.")
        @NotNull(message = "O estoque não pode ser nulo.")
        Integer stock
) {
}
