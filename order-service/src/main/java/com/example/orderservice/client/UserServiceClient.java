package com.example.orderservice.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class UserServiceClient {

    private final WebClient webClient;
    private final String userServiceUrl;

    public UserServiceClient(WebClient.Builder webClientBuilder, 
                           @Value("${user-service.url:http://localhost:8080}") String userServiceUrl) {
        this.webClient = webClientBuilder.build();
        this.userServiceUrl = userServiceUrl;
    }

    public Mono<User> getUserById(Long userId) {
        return webClient
                .get()
                .uri(userServiceUrl + "/api/users/{id}", userId)
                .retrieve()
                .bodyToMono(User.class)
                .doOnNext(user -> System.out.println("Fetched user: " + user.getName()))
                .doOnError(error -> System.err.println("Error fetching user: " + error.getMessage()));
    }

    public static class User {
        private Long id;
        private String name;
        private String email;

        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }
}