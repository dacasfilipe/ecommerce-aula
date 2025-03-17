package com.senai.ecommerce.dtos;

import com.senai.ecommerce.entities.OrderItem;
import lombok.Data;

@Data
public class OrderItemDTO {
    private Long orderId;
    private Long productId;
    private String productName;
    private int quantity;
    private double price;

    public OrderItemDTO() {
    }

    public OrderItemDTO(OrderItem entity) {
        this.orderId = entity.getId().getOrder().getId();
        this.productId = entity.getId().getProduct().getId();
        this.productName = entity.getId().getProduct().getName();
        this.quantity = entity.getQuantity();
        this.price = entity.getPrice();
    }
}
