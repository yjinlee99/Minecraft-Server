package com.minecraft.smallminecraft.order.controller;

import com.minecraft.smallminecraft.order.dtos.OrderRequest;
import com.minecraft.smallminecraft.order.dtos.OrderVerifyRequest;
import com.minecraft.smallminecraft.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/v1/create")
    public ResponseEntity<Object> createOrder(@RequestBody OrderRequest orderRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return orderService.createOrder(orderRequest, username);
    }

    @PostMapping("/v1/verify")
    public ResponseEntity<Object> verifyOrder(@RequestBody OrderVerifyRequest request) {
        return orderService.verifyOrder(request);
    }
}
