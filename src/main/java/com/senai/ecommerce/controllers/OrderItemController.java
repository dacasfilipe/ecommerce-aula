package com.senai.ecommerce.controllers;

import com.senai.ecommerce.dtos.OrderItemDTO;
import com.senai.ecommerce.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
@RestController
@RequestMapping(value = "/api/orderitems")
public class OrderItemController {

    @Autowired
    private OrderItemService service;

    @GetMapping
    public ResponseEntity<List<OrderItemDTO>> findAll() {
        List<OrderItemDTO> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{orderId}/{productId}")
    public ResponseEntity<OrderItemDTO> findById(@PathVariable Long orderId, @PathVariable Long productId) {
        OrderItemDTO dto = service.findById(orderId, productId);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    public ResponseEntity<OrderItemDTO> insert(@RequestBody OrderItemDTO dto) {
        dto = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{orderId}/{productId}")
                .buildAndExpand(dto.getOrderId(), dto.getProductId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping(value = "/{orderId}/{productId}")
    public ResponseEntity<OrderItemDTO> update(
            @PathVariable Long orderId,
            @PathVariable Long productId,
            @RequestBody OrderItemDTO dto) {
        dto = service.update(orderId, productId, dto);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping(value = "/{orderId}/{productId}")
    public ResponseEntity<Void> delete(@PathVariable Long orderId, @PathVariable Long productId) {
        service.delete(orderId, productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-order/{orderId}")
    public ResponseEntity<List<OrderItemDTO>> findByOrderId(@PathVariable Long orderId) {
        List<OrderItemDTO> items = service.findByOrderId(orderId);
        return ResponseEntity.ok().body(items);
    }
}
