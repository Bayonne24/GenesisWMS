package com.genesis.wms.controller;

import com.genesis.wms.dto.InventoryResponse;
import com.genesis.wms.dto.InventoryUpdateRequest;
import com.genesis.wms.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@Tag(name = "Inventory", description = "Inventory management endpoints")
public class InventoryController {

    private final InventoryService inventoryService;

    @PutMapping("/{sku}")
    @Operation(summary = "Set stock level for a product")
    public ResponseEntity<InventoryResponse> setStock(
            @PathVariable String sku,
            @Valid @RequestBody InventoryUpdateRequest request) {
        return ResponseEntity.ok(inventoryService.setStock(sku, request));
    }

    @PatchMapping("/{sku}/adjust")
    @Operation(summary = "Adjust stock by delta (positive to add, negative to reduce)")
    public ResponseEntity<InventoryResponse> adjustStock(
            @PathVariable String sku,
            @RequestParam int delta) {
        return ResponseEntity.ok(inventoryService.adjustQuantity(sku, delta));
    }

    @GetMapping("/{sku}")
    @Operation(summary = "Get stock level for a product")
    public ResponseEntity<InventoryResponse> getStock(@PathVariable String sku) {
        return ResponseEntity.ok(inventoryService.getStockLevel(sku));
    }

    @GetMapping("/low-stock")
    @Operation(summary = "Get all products below stock threshold")
    public ResponseEntity<List<InventoryResponse>> getLowStock(
            @RequestParam(defaultValue = "10") int threshold) {
        return ResponseEntity.ok(inventoryService.getLowStock(threshold));
    }
}