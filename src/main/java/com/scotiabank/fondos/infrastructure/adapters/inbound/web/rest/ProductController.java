package com.scotiabank.fondos.infrastructure.adapters.inbound.web.rest;

import com.scotiabank.fondos.domain.model.Product;
import com.scotiabank.fondos.domain.ports.in.ProductServicePort;
import com.scotiabank.fondos.infrastructure.adapters.inbound.web.dto.ProductRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductServicePort productService;

    public ProductController(ProductServicePort productService) {
        this.productService = productService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Product> create(@Valid @RequestBody ProductRequest request) {
        log.info("Rest request to create product: {}", request.code());
        return Mono.just(request)
                .map(req -> new Product(null, req.code(), req.name(), req.description(),
                        req.price(), req.category(), null, null, null))
                .flatMap(productService::createProduct);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Product>> getById(@PathVariable Integer id) {
        return productService.getProductById(id)
                .map(product -> ResponseEntity.ok().body(product))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @GetMapping
    public Flux<Product> getAll() {
        return productService.getAllProducts()
                .doOnComplete(() -> log.info("All products fetched successfully"));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Product>> update(@PathVariable Integer id, @Valid @RequestBody ProductRequest request) {
        return Mono.just(request)
                .map(req -> new Product(id, req.code(), req.name(), req.description(),
                        req.price(), req.category(), null, null, null))
                .flatMap(p -> productService.updateProduct(id, p))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable Integer id) {
        log.info("Rest request to delete product: {}", id);
        return productService.deleteProduct(id);
    }
}