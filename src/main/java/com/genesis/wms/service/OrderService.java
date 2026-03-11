package com.genesis.wms.service;

import com.genesis.wms.dto.OrderCreateRequest;
import com.genesis.wms.dto.OrderResponse;
import com.genesis.wms.entity.Order;
import com.genesis.wms.entity.OrderStatus;
import com.genesis.wms.exception.ResourceNotFoundException;
import com.genesis.wms.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderResponse createOrder(OrderCreateRequest request) {
        Order order = Order.builder()
                .referenceNumber(request.referenceNumber())
                .totalAmount(request.totalAmount())
                .build();
        return toResponse(orderRepository.save(order));
    }

    public OrderResponse getOrder(Long id) {
        return toResponse(findById(id));
    }

    public List<OrderResponse> listOrders(OrderStatus status) {
        List<Order> orders = status != null
                ? orderRepository.findAllByStatus(status)
                : orderRepository.findAll();
        return orders.stream().map(this::toResponse).toList();
    }

    public OrderResponse updateStatus(Long id, OrderStatus newStatus) {
        Order order = findById(id);
        order.setStatus(newStatus);
        return toResponse(orderRepository.save(order));
    }

    private Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
    }

    private OrderResponse toResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getReferenceNumber(),
                order.getStatus(),
                order.getPlacedAt(),
                order.getTotalAmount()
        );
    }
}