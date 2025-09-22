package com.example.service;

import com.example.model.CreateUserRequest;
import com.example.model.UpdateUserRequest;
import com.example.model.User;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserService {
    
    private final ConcurrentHashMap<Long, User> users = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);
    
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
    
    public User createUser(CreateUserRequest request) {
        User user = new User();
        user.setId(idCounter.getAndIncrement());
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setCreatedAt(OffsetDateTime.now());
        
        users.put(user.getId(), user);
        return user;
    }
    
    public User getUserById(Long userId) {
        User user = users.get(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return user;
    }
    
    public User updateUser(Long userId, UpdateUserRequest request) {
        User user = getUserById(userId);
        if (request.getName() != null) {
            user.setName(request.getName());
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        users.put(userId, user);
        return user;
    }
    
    public void deleteUser(Long userId) {
        if (users.remove(userId) == null) {
            throw new RuntimeException("User not found");
        }
    }
}