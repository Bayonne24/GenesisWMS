package com.genesis.wms.dto;

import jakarta.validation.constraints.Min;

public record InventoryUpdateRequest(
        @Min(value = 0, message = "Quantity cannot be negative")
        int quantity,
        String warehouseLocation
) {}