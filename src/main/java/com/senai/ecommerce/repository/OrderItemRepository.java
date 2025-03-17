package com.senai.ecommerce.repository;

import com.senai.ecommerce.entities.OrderItem;
import com.senai.ecommerce.entities.OrderItemPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemPK> {
    @Query("SELECT oi FROM OrderItem oi WHERE oi.id.order.id = :orderId")
    List<OrderItem> findByOrderId(@Param("orderId") Long orderId);
}
