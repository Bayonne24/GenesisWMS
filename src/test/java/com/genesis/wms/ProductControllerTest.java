package com.genesis.wms;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.genesis.wms.dto.ProductRequest;
import com.genesis.wms.repository.InventoryRepository;
import com.genesis.wms.repository.ProductRepository;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProductControllerTest extends AbstractIntegrationTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired ProductRepository productRepository;
    @Autowired
    InventoryRepository inventoryRepository;

    @BeforeEach
    void cleanup() {
        inventoryRepository.deleteAll();
        productRepository.deleteAll();
    }


    ProductRequest sampleRequest() {
        return new ProductRequest("SKU-001", "Test Product", "A description", "Electronics", new BigDecimal("29.99"));
    }

    @Test
    void createProduct_returns201() throws Exception {
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sku").value("SKU-001"))
                .andExpect(jsonPath("$.name").value("Test Product"));
    }

    @Test
    void getProductBySku_returnsProduct() throws Exception {
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleRequest())));

        mockMvc.perform(get("/api/products/SKU-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sku").value("SKU-001"));
    }

    @Test
    void createProduct_duplicateSku_returns409() throws Exception {
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleRequest())));

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequest())))
                .andExpect(status().isConflict());
    }

    @Test
    void deleteProduct_returns204() throws Exception {
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleRequest())));

        mockMvc.perform(delete("/api/products/SKU-001"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getProduct_afterDelete_returns404() throws Exception {
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleRequest())));

        mockMvc.perform(delete("/api/products/SKU-001"));

        mockMvc.perform(get("/api/products/SKU-001"))
                .andExpect(status().isNotFound());
    }
}