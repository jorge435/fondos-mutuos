package com.scotiabank.fondos.infrastructure.adapters.outbound.persistence.repository;
import com.scotiabank.fondos.infrastructure.adapters.outbound.persistence.entity.ProductEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<ProductEntity, Integer> {
}