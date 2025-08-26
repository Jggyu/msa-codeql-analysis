package com.example.userservice.service;

import com.example.userservice.model.User;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserService {

    private final Map<Long, User> users = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong();

    public UserService() {
        // Initialize with some dummy data
        createUser(new User(null, "John Doe", "john@example.com"));
        createUser(new User(null, "Jane Smith", "jane@example.com"));
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public User getUserById(Long id) {
        return users.get(id);
    }

    public User createUser(User user) {
        Long id = idCounter.incrementAndGet();
        user.setId(id);
        users.put(id, user);
        return user;
    }

    public void deleteUser(Long id) {
        users.remove(id);
    }
}