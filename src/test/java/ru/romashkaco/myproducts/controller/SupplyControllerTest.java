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
import ru.romashkaco.myproducts.model.Supply;
import ru.romashkaco.myproducts.service.SupplyService;

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
@WebMvcTest(SupplyController.class)
class SupplyControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    SupplyService supplyService;

    @Autowired
    ObjectMapper objectMapper;

    Supply supply;
    Product product;
    final String URL = "/api/supplies";

    @BeforeEach
    void setUp() {
        product = new Product(1L, "Test Product", "Test Product Description", 100L, true);
        supply = new Supply(1L, "Test Supply", product, 10);
    }

    @Test
    void testCreateSupply() throws Exception {
        when(supplyService.create(any(Supply.class))).thenReturn(supply);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(supply)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.documentName").value("Test Supply"))
                .andExpect(jsonPath("$.product.id").value(1L))
                .andExpect(jsonPath("$.product.name").value("Test Product"))
                .andExpect(jsonPath("$.quantity").value(10));

        verify(supplyService, times(1)).create(any(Supply.class));
    }

    @Test
    void testUpdateSupply() throws Exception {
        when(supplyService.update(anyLong(), any(Supply.class))).thenReturn(supply);

        mockMvc.perform(put(URL + "/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(supply)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.documentName").value("Test Supply"))
                .andExpect(jsonPath("$.product.id").value(1L))
                .andExpect(jsonPath("$.product.name").value("Test Product"))
                .andExpect(jsonPath("$.quantity").value(10));

        verify(supplyService, times(1)).update(anyLong(), any(Supply.class));
    }

    @Test
    void testGetSupply() throws Exception {
        when(supplyService.getSupplyById(anyLong())).thenReturn(supply);

        mockMvc.perform(get(URL + "/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.documentName").value("Test Supply"))
                .andExpect(jsonPath("$.product.id").value(1L))
                .andExpect(jsonPath("$.product.name").value("Test Product"))
                .andExpect(jsonPath("$.quantity").value(10));

        verify(supplyService, times(1)).getSupplyById(anyLong());
    }

    @Test
    void testDeleteSupply() throws Exception {
        doNothing().when(supplyService).delete(anyLong());

        mockMvc.perform(delete(URL + "/{id}", 1L))
                .andExpect(status().isOk());

        verify(supplyService, times(1)).delete(anyLong());
    }
}
