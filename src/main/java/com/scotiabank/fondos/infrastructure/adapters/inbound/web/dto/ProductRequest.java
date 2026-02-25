package com.scotiabank.fondos.infrastructure.adapters.inbound.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank(message = "El código es obligatorio")
        @Size(max = 10) String code,

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100) String name,

        String description,

        @NotNull @Positive(message = "El precio debe ser mayor a 0")
        BigDecimal price,

        @NotBlank String category
) {}