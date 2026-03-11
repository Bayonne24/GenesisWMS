package com.genesis.wms.repository;

import com.genesis.wms.entity.Order;
import com.genesis.wms.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByStatus(OrderStatus status);
}