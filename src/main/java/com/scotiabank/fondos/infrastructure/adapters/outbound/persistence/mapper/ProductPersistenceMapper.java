package com.scotiabank.fondos.infrastructure.adapters.outbound.persistence.mapper;

import com.scotiabank.fondos.domain.enums.ProductStatus;
import com.scotiabank.fondos.domain.model.Product;
import com.scotiabank.fondos.infrastructure.adapters.outbound.persistence.entity.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductPersistenceMapper {
    public Product toDomain(ProductEntity entity) {
        return new Product(
                entity.id(), entity.code(), entity.name(), entity.description(),
                entity.price(), entity.category(), entity.regDate(), entity.modDate(),
                ProductStatus.fromBoolean(entity.state()) // Convertimos Boolean a Enum
        );
    }

    public ProductEntity toEntity(Product domain) {
        return new ProductEntity(
                domain.id(), domain.code(), domain.name(), domain.description(),
                domain.price(), domain.category(), domain.regDate(), domain.modDate(),
                domain.state() == ProductStatus.ACTIVE // Convertimos Enum a Boolean
        );
    }
}
