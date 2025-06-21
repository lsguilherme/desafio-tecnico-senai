package com.example.desafiosenai.dtos.requests;

import com.example.desafiosenai.entities.CouponType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CouponRequestDto(

        @NotBlank(message = "O campo 'code' não pode estar em branco.")
        @Size(min = 3, max = 50, message = "O campo 'code' deve ter entre 3 e 50 caracteres.")
        @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "O campo 'code' contém caracteres inválidos.")
        String code,

        @NotNull(message = "O campo 'type' não pode ser nulo.")
        CouponType type,

        @NotNull(message = "O campo 'value' não pode ser nulo.")
        @DecimalMin(value = "0.01", message = "O campo 'value' (valor de desconto) deve ser maior que zero.")
        @JsonProperty("value")
        BigDecimal discountValue,

        @NotNull(message = "O campo 'one_shot' não pode ser nulo.")
        Boolean oneShot,

        @JsonProperty("max_uses")
        Integer maxUses,

        @NotNull(message = "O campo 'valid_from' não pode ser nulo.")
        @JsonProperty("valid_from")
        LocalDateTime validFrom,

        @NotNull(message = "O campo 'valid_until' não pode ser nulo.")
        @JsonProperty("valid_until")
        LocalDateTime validUntil

) {

}
