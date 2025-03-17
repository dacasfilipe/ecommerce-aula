package com.senai.ecommerce.dtos;

import com.senai.ecommerce.entities.Order;
import com.senai.ecommerce.entities.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderDTO {
    private Long id;
    private LocalDateTime moment;
    private OrderStatus status;
    private Long userId;
    private String userName;
    private Long paymentId;

    public OrderDTO() {
    }

    public OrderDTO(Order entity) {
        this.id = entity.getId();
        this.moment = entity.getMoment();
        this.status = entity.getStatus();
        this.userId = entity.getUser().getId();
        this.userName = entity.getUser().getName();
        this.paymentId = entity.getPayment() != null ? entity.getPayment().getId() : null;
    }
}
