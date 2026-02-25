package com.scotiabank.fondos.domain.ports.in;

import com.scotiabank.fondos.domain.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductServicePort {
    Mono<Product> createProduct(Product product);
    Mono<Product> getProductById(Integer id);
    Flux<Product> getAllProducts();
    Mono<Product> updateProduct(Integer id, Product product);
    Mono<Void> deleteProduct(Integer id);
}
