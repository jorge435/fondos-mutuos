package com.scotiabank.fondos.infrastructure.adapters.inbound.web.rest;

import com.scotiabank.fondos.domain.enums.ProductStatus;
import com.scotiabank.fondos.domain.model.Product;
import com.scotiabank.fondos.domain.ports.in.ProductServicePort;
import com.scotiabank.fondos.infrastructure.adapters.inbound.web.dto.ProductRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductServicePort productService;

    private ProductController controller;

    @BeforeEach
    void setUp() {
        controller = new ProductController(productService);
    }

    @Test
    @DisplayName("Debe retornar 201 (Created) al crear un producto exitosamente")
    void shouldCreateProduct() {

        var request = createValidRequest();
        var savedProduct = mockProduct(1, "FND001");

        when(productService.createProduct(any(Product.class)))
                .thenReturn(Mono.just(savedProduct));


        StepVerifier.create(controller.create(request))
                .assertNext(product -> {
                    assertEquals(1, product.id());
                    assertEquals("FND001", product.code());
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe retornar 200 (OK) cuando el producto existe")
    void shouldReturnOkWhenFound() {

        int id = 3;
        when(productService.getProductById(id))
                .thenReturn(Mono.just(mockProduct(id, "FND003")));


        StepVerifier.create(controller.getById(id))
                .assertNext(response -> {
                    assertEquals(HttpStatus.OK, response.getStatusCode());
                    assertNotNull(response.getBody());
                    assertEquals(id, response.getBody().id());
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe retornar 404 (Not Found) cuando el producto no existe")
    void shouldReturnNotFoundWhenMissing() {

        int id = 404;
        when(productService.getProductById(id)).thenReturn(Mono.empty());


        StepVerifier.create(controller.getById(id))
                .assertNext(response -> {
                    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
                    assertNull(response.getBody());
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe retornar 404 al intentar actualizar un producto inexistente")
    void shouldReturnNotFoundOnUpdateFailure() {

        int id = 404;
        when(productService.updateProduct(anyInt(), any(Product.class)))
                .thenReturn(Mono.empty());


        StepVerifier.create(controller.update(id, createValidRequest()))
                .assertNext(response -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()))
                .verifyComplete();
    }

    private ProductRequest createValidRequest() {
        return new ProductRequest("FND001", "Fondo", "Desc", new BigDecimal("1500"), "Renta");
    }

    private Product mockProduct(int id, String code) {
        return new Product(id, code, "Fondo", "Desc", new BigDecimal("1500"), "Renta",
                LocalDateTime.now(), null, ProductStatus.ACTIVE);
    }
}
