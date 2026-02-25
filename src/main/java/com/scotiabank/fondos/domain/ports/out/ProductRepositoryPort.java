package com.scotiabank.fondos.domain.ports.out;

import com.scotiabank.fondos.domain.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepositoryPort {
    Mono<Product> save(Product product);
    Mono<Product> findById(Integer id);
    Flux<Product> findAll();
    Mono<Void> deleteById(Integer id);
}
