package ru.romashkaco.myproducts.service.impl;

import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.romashkaco.myproducts.exception.ResourceNotFoundException;
import ru.romashkaco.myproducts.model.Product;
import ru.romashkaco.myproducts.model.Supply;
import ru.romashkaco.myproducts.repository.ProductRepository;
import ru.romashkaco.myproducts.repository.SupplyRepository;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestPropertySource(properties = {
        "server.port=8080"
})
@SpringBootTest
@Testcontainers
@FieldDefaults(level = PRIVATE)
class SupplyServiceImplCRUDTest {

    @Container
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("user")
            .withPassword("password");

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
        registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.PostgreSQLDialect");
    }

    @Autowired
    SupplyServiceImpl supplyService;

    @Autowired
    SupplyRepository supplyRepository;

    @Autowired
    ProductRepository productRepository;

    Product product;
    Supply supply;

    @BeforeEach
    void setUp() {
        supplyRepository.deleteAll();
        productRepository.deleteAll();

        product = new Product(1L, "Test Product", "Test Description", 100L, true);
        product = productRepository.save(product);
        supply = new Supply(null, "Test Supply", product, 100);
    }

    @Test
    void testCreateSupply() {
        var createdSupply = supplyService.create(supply);
        assertThat(createdSupply.getId()).isNotNull();

        assertThat(createdSupply.getDocumentName()).isEqualTo("Test Supply");
        assertThat(createdSupply.getProduct().getId()).isEqualTo(product.getId());

        var fetchedProduct = productRepository.findById(product.getId()).orElse(null);
        assertThat(fetchedProduct).isNotNull();
        assertThat(fetchedProduct.isInStock()).isTrue();
    }

    @Test
    void testGetSupplyById() {
        var createdSupply = supplyService.create(supply);
        var fetchedSupply = supplyService.getSupplyById(createdSupply.getId());

        assertThat(fetchedSupply.getId()).isEqualTo(createdSupply.getId());
        assertThat(fetchedSupply.getDocumentName()).isEqualTo(createdSupply.getDocumentName());
    }

    @Test
    void testUpdateSupply() {
        var createdSupply = supplyService.create(supply);

        createdSupply.setDocumentName("Updated Supply");
        createdSupply.setQuantity(200);
        var updatedSupply = supplyService.update(createdSupply.getId(), createdSupply);

        assertThat(updatedSupply.getDocumentName()).isEqualTo("Updated Supply");
        assertThat(updatedSupply.getQuantity()).isEqualTo(200L);
    }

    @Test
    void testDeleteSupply() {
        var createdSupply = supplyService.create(supply);
        supplyService.delete(createdSupply.getId());
        assertThrows(ResourceNotFoundException.class, () -> supplyService.getSupplyById(createdSupply.getId()));

        var deletedSupply = supplyRepository.findById(createdSupply.getId()).orElse(null);
        assertThat(deletedSupply).isNull();
    }

    @Test
    void testGetAllSupplies() {
        supplyService.create(new Supply(null, "Supply 1", product, 100));
        supplyService.create(new Supply(null, "Supply 2", product, 200));
        supplyService.create(new Supply(null, "Supply 3", product, 300));

        var supplies = supplyService.getAllSupply();

        assertThat(supplies).hasSize(3);
        assertThat(supplies).extracting(Supply::getDocumentName).containsExactlyInAnyOrder("Supply 1", "Supply 2", "Supply 3");
    }
}
