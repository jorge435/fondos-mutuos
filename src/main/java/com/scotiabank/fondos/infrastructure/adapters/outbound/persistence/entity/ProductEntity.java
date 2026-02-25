package com.scotiabank.fondos.infrastructure.adapters.outbound.persistence.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table("products")
public record ProductEntity(
        @Id Integer id,
        @Column("code") String code,
        @Column("name") String name,
        @Column("description") String description,
        @Column("price") BigDecimal price,
        @Column("category") String category,
        @Column("reg_date") LocalDateTime regDate,
        @Column("mod_date") LocalDateTime modDate,
        @Column("state") Boolean state
) {}