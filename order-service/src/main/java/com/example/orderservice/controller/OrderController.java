package com.example.orderservice.controller;

import com.example.orderservice.model.Order;
import com.example.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        return orderService.createOrder(order);
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
    }

    // New endpoints that demonstrate inter-service communication
    @GetMapping("/{id}/with-user")
    public Mono<Order> getOrderWithUserInfo(@PathVariable Long id) {
        return orderService.getOrderWithUserInfo(id);
    }

    @GetMapping("/{id}/validate-user")
    public Mono<Boolean> validateOrderUser(@PathVariable Long id) {
        return orderService.validateOrderUser(id);
    }
}