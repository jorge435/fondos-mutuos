package com.scotiabank.fondos.infrastructure.adapters.inbound.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Schema(name = "ProductRequest", description = "Payload para crear o actualizar un producto")
public record ProductRequest(
        @Schema(description = "Codigo unico del producto", example = "FND001")
        @NotBlank(message = "El codigo es obligatorio")
        @Size(max = 10) String code,

        @Schema(description = "Nombre comercial del producto", example = "Fondo Conservador")
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100) String name,

        @Schema(description = "Descripcion opcional del producto", example = "Fondo orientado a bajo riesgo")
        String description,

        @Schema(description = "Precio unitario del producto", example = "1500.50")
        @NotNull
        @Positive(message = "El precio debe ser mayor a 0")
        BigDecimal price,

        @Schema(description = "Categoria del producto", example = "Renta Fija")
        @NotBlank(message = "La categoria es obligatoria")
        String category
) {
}
