package com.scotiabank.fondos.infrastructure.adapters.outbound.persistence;

import com.scotiabank.fondos.domain.model.Product;
import com.scotiabank.fondos.domain.ports.out.ProductRepositoryPort;
import com.scotiabank.fondos.infrastructure.adapters.outbound.persistence.mapper.ProductPersistenceMapper;
import com.scotiabank.fondos.infrastructure.adapters.outbound.persistence.repository.ProductRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class ProductRepositoryAdapter implements ProductRepositoryPort {
    private final ProductRepository repository;
    private final ProductPersistenceMapper mapper;

    public ProductRepositoryAdapter(ProductRepository repository, ProductPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Product> save(Product product) {
        return repository.save(mapper.toEntity(product)).map(mapper::toDomain);
    }

    @Override
    public Mono<Product> findById(Integer id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Flux<Product> findAll() {
        return repository.findAll().map(mapper::toDomain);
    }

    @Override
    public Mono<Void> deleteById(Integer id) {
        return repository.deleteById(id);
    }
}
