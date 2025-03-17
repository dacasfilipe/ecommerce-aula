package com.senai.ecommerce.service;

import com.senai.ecommerce.dtos.OrderDTO;
import com.senai.ecommerce.entities.Order;
import com.senai.ecommerce.repository.OrderRepository;
import com.senai.ecommerce.repository.UserRepository;
import com.senai.ecommerce.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Transactional(readOnly = true)
    public List<OrderDTO> findAll() {
        List<Order> list = repository.findAll();
        return list.stream().map(x -> new OrderDTO(x)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrderDTO findById(Long id) {
        Order order = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        // Use Hibernate.initialize to safely load the collection
        org.hibernate.Hibernate.initialize(order.getItems());
        return new OrderDTO(order);
    }

    @Transactional
    public OrderDTO insert(OrderDTO dto) {
        Order entity = new Order();
        // Set the current moment if not provided
        if (dto.getMoment() == null) {
            dto.setMoment(LocalDateTime.now());
        }
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new OrderDTO(entity);
    }

    @Transactional
    public OrderDTO update(Long id, OrderDTO dto) {
        Order entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new OrderDTO(entity);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Resource not found");
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<OrderDTO> findByUser(Long userId) {
        List<Order> orders = repository.findByUserId(userId);
        return orders.stream()
                .map(order -> {
                    org.hibernate.Hibernate.initialize(order.getItems());
                    return new OrderDTO(order);
                })
                .collect(Collectors.toList());
    }

    private void copyDtoToEntity(OrderDTO dto, Order entity) {
        entity.setMoment(dto.getMoment());
        entity.setStatus(dto.getStatus());
        
        entity.setUser(userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found")));
        
        if (dto.getPaymentId() != null) {
            entity.setPayment(paymentRepository.findById(dto.getPaymentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Payment not found")));
        }
    }
}
