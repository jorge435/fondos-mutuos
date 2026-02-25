package com.scotiabank.fondos.application;

import com.scotiabank.fondos.domain.enums.ProductStatus;
import com.scotiabank.fondos.domain.model.Product;
import com.scotiabank.fondos.domain.ports.out.ProductRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class ProductUseCaseTest {

    @Mock
    private ProductRepositoryPort repositoryPort;

    private ProductUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new ProductUseCase(repositoryPort);
    }

    @Test
    @DisplayName("Debe asignar valores por defecto y guardar el producto correctamente")
    void shouldCreateProduct() {

        Product request = createProductRequest();

        when(repositoryPort.save(any(Product.class))).thenAnswer(invocation -> {
            Product productToSave = invocation.getArgument(0);
            return Mono.just(mockSavedProduct(productToSave));
        });


        StepVerifier.create(useCase.createProduct(request))
                .assertNext(saved -> {
                    assertEquals(10, saved.id());
                    assertEquals(ProductStatus.ACTIVE, saved.state());
                    assertNotNull(saved.regDate());
                })
                .verifyComplete();

        verify(repositoryPort).save(any(Product.class));
    }

    @Test
    @DisplayName("Debe actualizar campos permitidos cuando el producto existe")
    void shouldUpdateExistingProduct() {

        int productId = 7;
        Product existing = createExistingProduct(productId);
        Product input = createUpdateInput();

        when(repositoryPort.findById(productId)).thenReturn(Mono.just(existing));
        when(repositoryPort.save(any(Product.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(useCase.updateProduct(productId, input))
                .assertNext(updated -> {
                    assertEquals(productId, updated.id());
                    assertNotNull(updated.modDate());
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe lanzar error si el producto a eliminar no existe")
    void shouldFailWhenProductNotFound() {
        int id = 99;
        when(repositoryPort.findById(id)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.deleteProduct(id))
                .expectError(RuntimeException.class)
                .verify();
    }


    private Product createProductRequest() {
        return new Product(null, "FND001", "Fondo", "Desc", new BigDecimal("100"), "Cat", null, null, null);
    }

    private Product createExistingProduct(int id) {
        return new Product(id, "OLD", "Name", "Desc", BigDecimal.ONE, "Cat", LocalDateTime.now(), null, ProductStatus.ACTIVE);
    }

    private Product createUpdateInput() {
        return new Product(null, "NEW", "New Name", "New Desc", BigDecimal.TEN, "Cat", null, null, null);
    }

    private Product mockSavedProduct(Product p) {
        return new Product(10, p.code(), p.name(), p.description(), p.price(), p.category(), p.regDate(), p.modDate(), p.state());
    }
}