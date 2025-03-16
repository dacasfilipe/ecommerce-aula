package com.senai.ecommerce.controllers;


import com.senai.ecommerce.dtos.PaymentDTO;
import com.senai.ecommerce.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@CrossOrigin(origins = "http://127.0.0.1:5500")

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    @Autowired
    PaymentService paymentService;
    @RequestMapping(value = "/{id}")
    public PaymentDTO getPaymentById(@PathVariable Long id) {
        return paymentService.findById(id);
    }
}
