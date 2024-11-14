package ru.romashkaco.myproducts.service;

import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Assertions;
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
import ru.romashkaco.myproducts.repository.ProductRepository;

import static lombok.AccessLevel.PRIVATE;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertThrows;

@TestPropertySource(properties = {
        "server.port=8080"
})
@SpringBootTest
@Testcontainers
@FieldDefaults(level = PRIVATE)
public class ProductServiceImplCRUDTest {
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
    ProductServiceImpl productService;

    @Autowired
    ProductRepository productRepository;

    Product product;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        product = new Product(1L, "Test Product", "Test Description", 100L, true);
    }

    @Test
    void testCreateProduct() {
        var createdProduct = productService.create(product);
        assertNotNull(createdProduct.getId());

        Assertions.assertEquals("Test Product", createdProduct.getName());
        Assertions.assertEquals(100, createdProduct.getPrice());
    }

    @Test
    void testGetProductById() {
        var createdProduct = productService.create(product);
        var fetchedProduct = productService.getProductById(createdProduct.getId());

        Assertions.assertEquals(createdProduct.getId(), fetchedProduct.getId());
        Assertions.assertEquals(createdProduct.getName(), fetchedProduct.getName());
    }

    @Test
    void testUpdateProduct() {
        var createdProduct = productService.create(product);

        createdProduct.setName("Updated Product");
        createdProduct.setPrice(200L);
        var updatedProduct = productService.updateProduct(createdProduct.getId(), createdProduct);

        Assertions.assertEquals("Updated Product", updatedProduct.getName());
        Assertions.assertEquals(200L, updatedProduct.getPrice());
    }

    @Test
    void testDeleteProduct() {
        var createdProduct = productService.create(product);
        productService.delete(createdProduct.getId());
        assertThrows(ResourceNotFoundException.class, () -> productService.getProductById(createdProduct.getId()));
    }

    @Test
    void testGetAllProducts() {
        productService.create(new Product(1L, "Product 1", "Description 1", 100L, true));
        productService.create(new Product(2L, "Product 2", "Description 2", 150L, false));
        productService.create(new Product(3L, "Product 3", "Description 3", 200L, true));

        var products = productService.getAllProducts();

        Assertions.assertEquals(3, products.size());
    }
}
