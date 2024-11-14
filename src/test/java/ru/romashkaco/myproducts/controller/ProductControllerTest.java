package ru.romashkaco.myproducts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import ru.romashkaco.myproducts.dto.ProductFilterRequest;
import ru.romashkaco.myproducts.exception.ResourceNotFoundException;
import ru.romashkaco.myproducts.model.Product;
import ru.romashkaco.myproducts.service.ProductServiceImpl;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(properties = {
        "server.port=8080"
})
@FieldDefaults(level = PRIVATE)
@WebMvcTest(ProductController.class)
class ProductControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProductServiceImpl productService;

    @Autowired
    ObjectMapper objectMapper;

    Product product;
    final String URL = "/api/products";

    @BeforeEach
    void setUp() {
        product = new Product(1L, "Test Product", "Description product", 1000L, true);
    }

    @Test
    void testGetFilteredProducts() throws Exception {
        var filReq = new ProductFilterRequest();
        filReq.setMinPrice(50L);
        filReq.setMaxPrice(150L);
        filReq.setInStock(true);
        filReq.setSortBy("price");
        filReq.setAscending(true);
        filReq.setPage(0);
        filReq.setSize(10);

        var product1 = new Product(1L, "Product A", "Description A", 100L, true);
        var product2 = new Product(2L, "Product C", "Description C", 150L, true);

        when(productService.getFilteredProducts(filReq)).thenReturn(List.of(product1, product2));

        var resultActions = mockMvc.perform(get(URL + "/filter")
                .param("minPrice", "50")
                .param("maxPrice", "150")
                .param("inStock", "true")
                .param("sortBy", "price")
                .param("ascending", "true")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Product A"))
                .andExpect(jsonPath("$[0].price").value(100))
                .andExpect(jsonPath("$[1].name").value("Product C"))
                .andExpect(jsonPath("$[1].price").value(150));

        verify(productService, times(1)).getFilteredProducts(filReq);
    }

    @Test
    void testGetAllProducts() throws Exception {
        when(productService.getAllProducts()).thenReturn(List.of(product));
        mockMvc.perform(get(URL)).andExpect(jsonPath("$[0].name").value(product.getName()))
                .andExpect(jsonPath("$[0].description").value(product.getDescription()))
                .andExpect(jsonPath("$[0].price").value(product.getPrice()));
    }

    @Test
    void testGetProductById() throws Exception {
        when(productService.getProductById(anyLong())).thenReturn(product);
        mockMvc.perform(get(URL + "/{id}", 1L))
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.description").value(product.getDescription()))
                .andExpect(jsonPath("$.price").value(product.getPrice()));
    }

    @Test
    void testGetProductById_notFound() throws Exception {
        when(productService.getProductById(anyLong())).thenThrow(ResourceNotFoundException.class);
        mockMvc.perform(get(URL + "/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateProduct() throws Exception {
        when(productService.create(any(Product.class))).thenReturn(product);
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.description").value(product.getDescription()))
                .andExpect(jsonPath("$.price").value(product.getPrice()));
    }

    @Test
    void testUpdateProduct() throws Exception {
        when(productService.updateProduct(anyLong(), any(Product.class))).thenReturn(product);
        mockMvc.perform(put(URL + "/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.description").value(product.getDescription()))
                .andExpect(jsonPath("$.price").value(product.getPrice()));
    }

    @Test
    void testDeleteProduct() throws Exception {
        doNothing().when(productService).delete(anyLong());
        var productId = product.getId();
        mockMvc.perform(delete(URL + "/" + productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Operation successful"));
        verify(productService, times(1)).delete(productId);
    }

    @Test
    void testDeleteProduct_notFound() throws Exception {
        doThrow(new ResourceNotFoundException(1L)).when(productService).delete(anyLong());
        mockMvc.perform(delete(URL + "/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateProduct_badRequest() throws Exception {
        var invalidProduct = new Product();
        invalidProduct.setId(null);
        invalidProduct.setName("");
        invalidProduct.setDescription("Super long description...".repeat(100));
        invalidProduct.setPrice(-1L);
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidProduct)))
                .andExpect(status().isBadRequest());

    }
}