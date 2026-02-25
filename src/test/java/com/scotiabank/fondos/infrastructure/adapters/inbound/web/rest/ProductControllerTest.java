package com.scotiabank.fondos.infrastructure.adapters.inbound.web.rest;

import com.scotiabank.fondos.domain.enums.ProductStatus;
import com.scotiabank.fondos.domain.model.Product;
import com.scotiabank.fondos.domain.ports.in.ProductServicePort;
import com.scotiabank.fondos.infrastructure.adapters.inbound.web.dto.ProductRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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
    void create_ShouldMapRequestAndReturnCreatedProduct() {
        ProductRequest request = new ProductRequest(
                "FND001",
                "Fondo Conservador",
                "Bajo riesgo",
                new BigDecimal("1500.50"),
                "Renta Fija"
        );

        Product saved = new Product(
                1,
                "FND001",
                "Fondo Conservador",
                "Bajo riesgo",
                new BigDecimal("1500.50"),
                "Renta Fija",
                LocalDateTime.now(),
                null,
                ProductStatus.ACTIVE
        );

        when(productService.createProduct(any(Product.class))).thenReturn(Mono.just(saved));

        StepVerifier.create(controller.create(request))
                .assertNext(product -> {
                    assertEquals(1, product.id());
                    assertEquals("FND001", product.code());
                })
                .verifyComplete();

        verify(productService).createProduct(any(Product.class));
    }

    @Test
    void getById_WhenFound_ShouldReturnOkResponse() {
        Product found = new Product(
                3,
                "FND003",
                "Fondo Variable",
                "Alto riesgo",
                new BigDecimal("2500.00"),
                "Renta Variable",
                LocalDateTime.now().minusDays(3),
                null,
                ProductStatus.ACTIVE
        );

        when(productService.getProductById(3)).thenReturn(Mono.just(found));

        StepVerifier.create(controller.getById(3))
                .assertNext(response -> {
                    assertEquals(200, response.getStatusCode().value());
                    assertEquals(3, response.getBody().id());
                })
                .verifyComplete();
    }

    @Test
    void getById_WhenNotFound_ShouldReturnNotFoundResponse() {
        when(productService.getProductById(404)).thenReturn(Mono.empty());

        StepVerifier.create(controller.getById(404))
                .assertNext(response -> {
                    HttpStatusCode statusCode = response.getStatusCode();
                    assertEquals(404, statusCode.value());
                    assertEquals(null, response.getBody());
                })
                .verifyComplete();
    }

    @Test
    void update_WhenNotFound_ShouldReturnNotFoundResponse() {
        ProductRequest request = new ProductRequest(
                "FND404",
                "No Existe",
                "Sin datos",
                new BigDecimal("10.00"),
                "Demo"
        );

        when(productService.updateProduct(any(Integer.class), any(Product.class))).thenReturn(Mono.empty());

        StepVerifier.create(controller.update(404, request))
                .assertNext(response -> assertEquals(ResponseEntity.notFound().build(), response))
                .verifyComplete();
    }
}
