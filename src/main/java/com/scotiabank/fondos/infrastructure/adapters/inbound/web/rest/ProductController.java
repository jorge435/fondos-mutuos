package com.scotiabank.fondos.infrastructure.adapters.inbound.web.rest;

import com.scotiabank.fondos.domain.model.Product;
import com.scotiabank.fondos.domain.ports.in.ProductServicePort;
import com.scotiabank.fondos.infrastructure.adapters.inbound.web.dto.ProductRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Products", description = "API reactiva para gestion de productos")
public class ProductController {

    private final ProductServicePort productService;

    public ProductController(ProductServicePort productService) {
        this.productService = productService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear producto", description = "Crea un nuevo producto con validaciones")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto creado",
                    content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "400", description = "Payload invalido", content = @Content)
    })
    public Mono<Product> create(@Valid @RequestBody ProductRequest request) {
        log.info("Rest request to create product: {}", request.code());
        return Mono.just(request)
                .map(req -> new Product(null, req.code(), req.name(), req.description(),
                        req.price(), req.category(), null, null, null))
                .flatMap(productService::createProduct);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado",
                    content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content)
    })
    public Mono<ResponseEntity<Product>> getById(@PathVariable Integer id) {
        return productService.getProductById(id)
                .map(product -> ResponseEntity.ok().body(product))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @GetMapping
    @Operation(summary = "Listar productos", description = "Retorna todos los productos registrados")
    @ApiResponse(responseCode = "200", description = "Listado de productos")
    public Flux<Product> getAll() {
        return productService.getAllProducts()
                .doOnComplete(() -> log.info("All products fetched successfully"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto", description = "Actualiza un producto existente por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado",
                    content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Payload invalido", content = @Content)
    })
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
    @Operation(summary = "Eliminar producto", description = "Elimina un producto por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Producto eliminado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content)
    })
    public Mono<Void> delete(@PathVariable Integer id) {
        log.info("Rest request to delete product: {}", id);
        return productService.deleteProduct(id);
    }
}
