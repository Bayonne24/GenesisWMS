package com.genesis.wms.service;

import com.genesis.wms.dto.InventoryUpdateRequest;
import com.genesis.wms.dto.InventoryResponse;
import com.genesis.wms.entity.Inventory;
import com.genesis.wms.exception.ResourceNotFoundException;
import com.genesis.wms.exception.StockException;
import com.genesis.wms.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductService productService;

    public InventoryResponse setStock(String sku, InventoryUpdateRequest request) {
        var product = productService.findBySku(sku);
        var inventory = inventoryRepository.findByProduct_Sku(sku)
                .orElse(Inventory.builder().product(product).build());
        inventory.setQuantity(request.quantity());
        inventory.setWarehouseLocation(request.warehouseLocation());
        return toResponse(inventoryRepository.save(inventory));
    }

    public InventoryResponse adjustQuantity(String sku, int delta) {
        var inventory = inventoryRepository.findByProduct_Sku(sku)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No inventory record found for SKU: " + sku));
        int newQuantity = inventory.getQuantity() + delta;
        if (newQuantity < 0) {
            throw new StockException("Insufficient stock for SKU: " + sku +
                    ". Available: " + inventory.getQuantity() + ", Requested reduction: " + Math.abs(delta));
        }
        inventory.setQuantity(newQuantity);
        return toResponse(inventoryRepository.save(inventory));
    }

    public InventoryResponse getStockLevel(String sku) {
        return toResponse(inventoryRepository.findByProduct_Sku(sku)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No inventory record found for SKU: " + sku)));
    }

    public List<InventoryResponse> getLowStock(int threshold) {
        return inventoryRepository.findAll().stream()
                .filter(i -> i.getQuantity() < threshold)
                .map(this::toResponse)
                .toList();
    }

    private InventoryResponse toResponse(Inventory inventory) {
        return new InventoryResponse(
                inventory.getId(),
                inventory.getProduct().getSku(),
                inventory.getWarehouseLocation(),
                inventory.getQuantity(),
                inventory.getLastUpdated()
        );
    }
}