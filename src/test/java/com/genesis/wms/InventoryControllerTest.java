package com.genesis.wms;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.genesis.wms.dto.InventoryUpdateRequest;
import com.genesis.wms.dto.ProductRequest;
import com.genesis.wms.repository.InventoryRepository;
import com.genesis.wms.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class InventoryControllerTest extends AbstractIntegrationTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired ProductRepository productRepository;
    @Autowired InventoryRepository inventoryRepository;



    void createProduct() throws Exception {
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        new ProductRequest("SKU-001", "Test Product", "Desc", "Electronics", new BigDecimal("9.99")))));
    }

    void setStock(int quantity) throws Exception {
        mockMvc.perform(put("/api/inventory/SKU-001")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        new InventoryUpdateRequest(quantity, "ZONE-A"))));
    }

    @Test
    void setStock_thenGetStock_returnsCorrectQuantity() throws Exception {
        createProduct();
        setStock(50);
        mockMvc.perform(get("/api/inventory/SKU-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(50));
    }

    @Test
    void adjustStock_add_increasesQuantity() throws Exception {
        createProduct();
        setStock(50);
        mockMvc.perform(patch("/api/inventory/SKU-001/adjust?delta=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(60));
    }

    @Test
    void adjustStock_reduce_decreasesQuantity() throws Exception {
        createProduct();
        setStock(50);
        mockMvc.perform(patch("/api/inventory/SKU-001/adjust?delta=-20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(30));
    }

    @Test
    void adjustStock_belowZero_returns409() throws Exception {
        createProduct();
        setStock(10);
        mockMvc.perform(patch("/api/inventory/SKU-001/adjust?delta=-50"))
                .andExpect(status().isConflict());
    }
}