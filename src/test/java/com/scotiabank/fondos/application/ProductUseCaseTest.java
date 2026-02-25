package com.scotiabank.fondos.application;

import com.scotiabank.fondos.domain.enums.ProductStatus;
import com.scotiabank.fondos.domain.model.Product;
import com.scotiabank.fondos.domain.ports.out.ProductRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
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
    void createProduct_ShouldSetDefaultFieldsAndSave() {
        Product request = new Product(
                null,
                "FND001",
                "Fondo Conservador",
                "Bajo riesgo",
                new BigDecimal("1500.50"),
                "Renta Fija",
                null,
                null,
                null
        );

        when(repositoryPort.save(any(Product.class))).thenAnswer(invocation -> {
            Product productToSave = invocation.getArgument(0);
            return Mono.just(new Product(
                    10,
                    productToSave.code(),
                    productToSave.name(),
                    productToSave.description(),
                    productToSave.price(),
                    productToSave.category(),
                    productToSave.regDate(),
                    productToSave.modDate(),
                    productToSave.state()
            ));
        });

        StepVerifier.create(useCase.createProduct(request))
                .assertNext(saved -> {
                    assertEquals(10, saved.id());
                    assertEquals("FND001", saved.code());
                    assertEquals(ProductStatus.ACTIVE, saved.state());
                    assertNotNull(saved.regDate());
                })
                .verifyComplete();

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(repositoryPort).save(captor.capture());

        Product sentToRepository = captor.getValue();
        assertEquals(ProductStatus.ACTIVE, sentToRepository.state());
        assertNotNull(sentToRepository.regDate());
        assertEquals(null, sentToRepository.modDate());
    }

    @Test
    void updateProduct_WhenProductExists_ShouldSaveUpdatedValues() {
        Product existing = new Product(
                7,
                "FND002",
                "Fondo Mixto",
                "Riesgo moderado",
                new BigDecimal("1000.00"),
                "Mixto",
                LocalDateTime.now().minusDays(2),
                null,
                ProductStatus.ACTIVE
        );

        Product input = new Product(
                null,
                "FND002-UPD",
                "Fondo Mixto Plus",
                "Ajustado",
                new BigDecimal("1200.00"),
                "Mixto",
                null,
                null,
                null
        );

        when(repositoryPort.findById(7)).thenReturn(Mono.just(existing));
        when(repositoryPort.save(any(Product.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(useCase.updateProduct(7, input))
                .assertNext(updated -> {
                    assertEquals(7, updated.id());
                    assertEquals("FND002-UPD", updated.code());
                    assertEquals(existing.regDate(), updated.regDate());
                    assertEquals(ProductStatus.ACTIVE, updated.state());
                    assertNotNull(updated.modDate());
                })
                .verifyComplete();

        verify(repositoryPort).findById(7);
        verify(repositoryPort).save(any(Product.class));
    }

    @Test
    void deleteProduct_WhenProductNotFound_ShouldReturnError() {
        when(repositoryPort.findById(99)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.deleteProduct(99))
                .expectErrorMatches(error ->
                        error instanceof RuntimeException
                                && "Product not found with id: 99".equals(error.getMessage()))
                .verify();

        verify(repositoryPort).findById(99);
    }
}
