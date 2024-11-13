package ru.romashkaco.myproducts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.romashkaco.myproducts.exception.ResourceNotFoundException;
import ru.romashkaco.myproducts.model.Product;
import ru.romashkaco.myproducts.service.ProductServiceIImpl;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@FieldDefaults(level = PRIVATE)
@WebMvcTest(ProductController.class)
class ProductControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProductServiceIImpl productService;

    @Autowired
    ObjectMapper objectMapper;

    Product product;
    final String URL = "/api/products";

    @BeforeEach
    void setUp() {
        product = generateProduct();
    }

    private Product generateProduct() {
        var product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setDescription("Description product");
        product.setPrice(1000L);
        return product;
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
        invalidProduct.setPrice(-1);
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidProduct)))
                .andExpect(status().isBadRequest());

    }

}