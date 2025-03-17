package com.senai.ecommerce.service;

import com.senai.ecommerce.dtos.OrderItemDTO;
import com.senai.ecommerce.entities.OrderItem;
import com.senai.ecommerce.entities.OrderItemPK;
import com.senai.ecommerce.repository.OrderItemRepository;
import com.senai.ecommerce.repository.OrderRepository;
import com.senai.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderItemService {

    @Autowired
    private OrderItemRepository repository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<OrderItemDTO> findAll() {
        List<OrderItem> list = repository.findAll();
        return list.stream().map(x -> new OrderItemDTO(x)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrderItemDTO findById(Long orderId, Long productId) {
        OrderItemPK id = new OrderItemPK();
        id.setOrder(orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found")));
        id.setProduct(productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found")));
        
        OrderItem orderItem = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OrderItem not found"));
        return new OrderItemDTO(orderItem);
    }

    @Transactional
    public OrderItemDTO insert(OrderItemDTO dto) {
        OrderItem entity = new OrderItem();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new OrderItemDTO(entity);
    }

    @Transactional
    public OrderItemDTO update(Long orderId, Long productId, OrderItemDTO dto) {
        OrderItemPK id = new OrderItemPK();
        id.setOrder(orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found")));
        id.setProduct(productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found")));
        
        OrderItem entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OrderItem not found"));
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new OrderItemDTO(entity);
    }

    @Transactional
    public void delete(Long orderId, Long productId) {
        OrderItemPK id = new OrderItemPK();
        id.setOrder(orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found")));
        id.setProduct(productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found")));
        
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("OrderItem not found");
        }
        repository.deleteById(id);
    }

    private void copyDtoToEntity(OrderItemDTO dto, OrderItem entity) {
        OrderItemPK pk = new OrderItemPK();
        pk.setOrder(orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found")));
        pk.setProduct(productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found")));
        
        entity.setId(pk);
        entity.setQuantity(dto.getQuantity());
        entity.setPrice(dto.getPrice());
    }

    @Transactional(readOnly = true)
    public List<OrderItemDTO> findByOrderId(Long orderId) {
        return repository.findByOrderId(orderId).stream()
                .map(OrderItemDTO::new)
                .collect(Collectors.toList());
    }
}
