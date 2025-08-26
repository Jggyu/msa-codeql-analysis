package com.example.orderservice.service;

import com.example.orderservice.client.UserServiceClient;
import com.example.orderservice.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class OrderService {

    @Autowired
    private UserServiceClient userServiceClient;

    private final Map<Long, Order> orders = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong();

    public OrderService() {
        // Initialize with some dummy data
        createOrder(new Order(null, 1L, "Laptop", 1, 1200.00));
        createOrder(new Order(null, 2L, "Mouse", 2, 25.99));
    }

    public List<Order> getAllOrders() {
        return new ArrayList<>(orders.values());
    }

    public Order getOrderById(Long id) {
        return orders.get(id);
    }

    public Order createOrder(Order order) {
        Long id = idCounter.incrementAndGet();
        order.setId(id);
        orders.put(id, order);
        return order;
    }

    public void deleteOrder(Long id) {
        orders.remove(id);
    }

    /**
     * Get order with enriched user information
     * This method demonstrates inter-service communication
     */
    public Mono<Order> getOrderWithUserInfo(Long orderId) {
        Order order = getOrderById(orderId);
        if (order == null) {
            return Mono.empty();
        }

        // Call User Service to get user details
        return userServiceClient.getUserById(order.getUserId())
                .map(user -> {
                    order.setUserName(user.getName());
                    return order;
                })
                .doOnNext(enrichedOrder -> 
                    System.out.println("Enriched order " + orderId + " with user info: " + enrichedOrder.getUserName())
                );
    }

    /**
     * Another example of calling User Service
     */
    public Mono<Boolean> validateOrderUser(Long orderId) {
        Order order = getOrderById(orderId);
        if (order == null) {
            return Mono.just(false);
        }

        return userServiceClient.getUserById(order.getUserId())
                .map(user -> user != null)
                .defaultIfEmpty(false);
    }
}