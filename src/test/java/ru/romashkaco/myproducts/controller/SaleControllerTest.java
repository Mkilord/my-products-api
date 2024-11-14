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
import ru.romashkaco.myproducts.model.Product;
import ru.romashkaco.myproducts.model.Sale;
import ru.romashkaco.myproducts.service.impl.SaleServiceImpl;

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
@WebMvcTest(SaleController.class)
class SaleControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    SaleServiceImpl saleService;

    @Autowired
    ObjectMapper objectMapper;

    Sale sale;
    Product product;
    final String URL = "/api/sales";

    @BeforeEach
    void setUp() {
        product = new Product(1L, "Product", "Product desc", 100L, true);
        sale = new Sale(1L, "Test Sale", product,  5, 500L);
    }

    @Test
    void testCreateSale() throws Exception {
        when(saleService.create(any(Sale.class))).thenReturn(sale);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sale)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.documentName").value("Test Sale"))
                .andExpect(jsonPath("$.quantitySold").value(5))
                .andExpect(jsonPath("$.purchasePrice").value(500));

        verify(saleService, times(1)).create(any(Sale.class));
    }

    @Test
    void testUpdateSale() throws Exception {
        when(saleService.update(anyLong(), any(Sale.class))).thenReturn(sale);

        mockMvc.perform(put(URL + "/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sale)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.documentName").value("Test Sale"))
                .andExpect(jsonPath("$.quantitySold").value(5))
                .andExpect(jsonPath("$.purchasePrice").value(500));

        verify(saleService, times(1)).update(anyLong(), any(Sale.class));
    }

    @Test
    void testGetSaleById() throws Exception {
        when(saleService.getSaleById(anyLong())).thenReturn(sale);

        mockMvc.perform(get(URL + "/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.documentName").value("Test Sale"))
                .andExpect(jsonPath("$.quantitySold").value(5))
                .andExpect(jsonPath("$.purchasePrice").value(500));
    }

    @Test
    void testDeleteSale() throws Exception {
        doNothing().when(saleService).deleteSale(anyLong());

        mockMvc.perform(delete(URL + "/{id}", 1L))
                .andExpect(status().isOk());
        verify(saleService, times(1)).deleteSale(anyLong());
    }
}
