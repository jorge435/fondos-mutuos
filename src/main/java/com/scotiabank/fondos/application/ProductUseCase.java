package com.scotiabank.fondos.application;

import com.scotiabank.fondos.domain.enums.ProductStatus;
import com.scotiabank.fondos.domain.model.Product;
import com.scotiabank.fondos.domain.ports.in.ProductServicePort;
import com.scotiabank.fondos.domain.ports.out.ProductRepositoryPort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;

@Service
public class ProductUseCase implements ProductServicePort {
    private final ProductRepositoryPort repositoryPort;

    public ProductUseCase(ProductRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public Mono<Product> createProduct(Product product) {
        return Mono.just(product)
                .map(p -> new Product(
                        null,
                        p.code(),
                        p.name(),
                        p.description(),
                        p.price(),
                        p.category(),
                        LocalDateTime.now(),
                        null,
                        ProductStatus.ACTIVE
                ))
                .flatMap(repositoryPort::save);
    }

    @Override
    public Mono<Product> getProductById(Integer id) {
        return repositoryPort.findById(id);
    }

    @Override
    public Flux<Product> getAllProducts() {
        return repositoryPort.findAll();
    }

    @Override
    public Mono<Product> updateProduct(Integer id, Product product) {
        return repositoryPort.findById(id)
                .flatMap(existing -> repositoryPort.save(new Product(
                        existing.id(),
                        product.code(),
                        product.name(),
                        product.description(),
                        product.price(),
                        product.category(),
                        existing.regDate(),
                        LocalDateTime.now(),
                        ProductStatus.ACTIVE
                )));
    }

    @Override
    public Mono<Void> deleteProduct(Integer id) {
        return repositoryPort.findById(id)
                .flatMap(p -> repositoryPort.deleteById(p.id()))
                .switchIfEmpty(Mono.error(new RuntimeException("Product not found with id: " + id)));
    }
}
