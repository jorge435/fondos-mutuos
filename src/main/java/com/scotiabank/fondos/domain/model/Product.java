package com.scotiabank.fondos.domain.model;

import com.scotiabank.fondos.domain.enums.ProductStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Product(
        Integer id,
        String code,
        String name,
        String description,
        BigDecimal price,
        String category,
        LocalDateTime regDate,
        LocalDateTime modDate,
        ProductStatus state
) {}