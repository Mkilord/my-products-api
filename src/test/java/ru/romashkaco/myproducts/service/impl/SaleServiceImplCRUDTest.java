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
import ru.romashkaco.myproducts.model.Sale;
import ru.romashkaco.myproducts.repository.ProductRepository;
import ru.romashkaco.myproducts.repository.SaleRepository;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestPropertySource(properties = {
        "server.port=8080"
})
@SpringBootTest
@Testcontainers
@FieldDefaults(level = PRIVATE)
public class SaleServiceImplCRUDTest {
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
    SaleServiceImpl saleService;

    @Autowired
    SaleRepository saleRepository;

    @Autowired
    ProductRepository productRepository;

    Product product;
    Sale sale;

    @BeforeEach
    void setUp() {
        saleRepository.deleteAll();
        productRepository.deleteAll();

        product = new Product(1L, "Test Product", "Test Description", 100L, false);
        product = productRepository.save(product);
        sale = new Sale(null, "Test Sale", product, 5, 1000L);
    }

    @Test
    void testCreateSale() {
        var createdSale = saleService.create(sale);
        assertThat(createdSale.getId()).isNotNull();

        assertThat(createdSale.getDocumentName()).isEqualTo("Test Sale");
        assertThat(createdSale.getPurchasePrice()).isEqualTo(500);
        assertThat(createdSale.getProduct().getId()).isEqualTo(product.getId());

        var fetchedProduct = productRepository.findById(product.getId()).orElse(null);
        assertThat(fetchedProduct).isNotNull();
        assertThat(fetchedProduct.getPrice()).isEqualTo(100);
    }

    @Test
    void testGetSaleById() {
        var createdSale = saleService.create(sale);
        var fetchedSale = saleService.getSaleById(createdSale.getId());

        assertThat(fetchedSale.getId()).isEqualTo(createdSale.getId());
        assertThat(fetchedSale.getDocumentName()).isEqualTo(createdSale.getDocumentName());
        assertThat(fetchedSale.getPurchasePrice()).isEqualTo(createdSale.getPurchasePrice());
    }

    @Test
    void testUpdateSale() {
        var createdSale = saleService.create(sale);

        createdSale.setDocumentName("Updated Sale");
        createdSale.setQuantitySold(10);
        var updatedSale = saleService.update(createdSale.getId(), createdSale);

        assertThat(updatedSale.getDocumentName()).isEqualTo("Updated Sale");
        assertThat(updatedSale.getQuantitySold()).isEqualTo(10);
        assertThat(updatedSale.getPurchasePrice()).isEqualTo(500L);
    }

    @Test
    void testDeleteSale() {
        var createdSale = saleService.create(sale);
        saleService.deleteSale(createdSale.getId());
        assertThrows(ResourceNotFoundException.class, () -> saleService.getSaleById(createdSale.getId()));

        var deletedSale = saleRepository.findById(createdSale.getId()).orElse(null);
        assertThat(deletedSale).isNull();
    }

    @Test
    void testGetAllSales() {
        saleService.create(new Sale(null, "Sale 1", product, 5, 500L));
        saleService.create(new Sale(null, "Sale 2", product, 10, 1000L));
        saleService.create(new Sale(null, "Sale 3", product, 15, 1500L));

        var sales = saleService.getAllSales();

        assertThat(sales).hasSize(3);
        assertThat(sales).extracting(Sale::getDocumentName).containsExactlyInAnyOrder("Sale 1", "Sale 2", "Sale 3");
    }
}
