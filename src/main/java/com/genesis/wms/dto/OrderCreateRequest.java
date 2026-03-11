package com.genesis.wms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record OrderCreateRequest(
        @NotBlank(message = "Reference number is required")
        String referenceNumber,

        @NotNull(message = "Total amount is required")
        BigDecimal totalAmount
) {}