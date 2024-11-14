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
import ru.romashkaco.myproducts.dto.ProductFilterRequest;
import ru.romashkaco.myproducts.model.Product;
import ru.romashkaco.myproducts.repository.ProductRepository;

import static lombok.AccessLevel.PRIVATE;

@TestPropertySource(properties = {
        "server.port=8080"
})
@SpringBootTest
@Testcontainers
@FieldDefaults(level = PRIVATE)
class ProductServiceImplFilterTest {
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

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();

        var product1 = new Product(1L, "Product A", "Description A", 100L, true);
        var product2 = new Product(1L, "Product B", "Description B", 150L, false);
        var product3 = new Product(1L, "Product C", "Description C", 50L, true);

        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);
    }

    @Test
    void testGetFilteredProducts() {
        var filterReq = new ProductFilterRequest();
        filterReq.setMinPrice(50L);
        filterReq.setMaxPrice(150L);
        filterReq.setInStock(true);
        filterReq.setSortBy("price");
        filterReq.setAscending(true);
        filterReq.setPage(0);
        filterReq.setSize(10);

        var products = productService.getFilteredProducts(filterReq);

        Assertions.assertEquals(2, products.size());
        Assertions.assertTrue(products.get(0).getPrice() <= products.get(1).getPrice());
    }

    @Test
    void testGetFilteredAndSortedByName() {
        var filReq = new ProductFilterRequest();
        filReq.setSortBy("name");
        filReq.setAscending(true);
        filReq.setPage(0);
        filReq.setSize(10);

        var products = productService.getFilteredProducts(filReq);

        Assertions.assertEquals(3, products.size());
        Assertions.assertTrue(products.get(0).getName().compareTo(products.get(1).getName()) < 0);
    }

    @Test
    void testGetFilteredProductsWithPageRequest() {
        var filReq = new ProductFilterRequest();
        filReq.setMinPrice(50L);
        filReq.setMaxPrice(150L);
        filReq.setInStock(true);
        filReq.setPage(0);
        filReq.setSize(1);
        filReq.setSortBy("price");
        filReq.setAscending(true);

        var products = productService.getFilteredProducts(filReq);

        Assertions.assertEquals(1, products.size());
        Assertions.assertEquals("Product C", products.get(0).getName());
    }
}