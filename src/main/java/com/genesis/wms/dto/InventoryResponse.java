package com.genesis.wms.dto;

import java.time.Instant;

public record InventoryResponse(
        Long id,
        String sku,
        String warehouseLocation,
        Integer quantity,
        Instant lastUpdated
) {}