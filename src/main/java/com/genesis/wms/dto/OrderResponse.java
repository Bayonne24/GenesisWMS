package com.genesis.wms.dto;

import com.genesis.wms.entity.OrderStatus;
import java.math.BigDecimal;
import java.time.Instant;

public record OrderResponse(
        Long id,
        String referenceNumber,
        OrderStatus status,
        Instant placedAt,
        BigDecimal totalAmount
) {}