package com.zorvyn.finance.service;

import com.zorvyn.finance.dto.UserDTO;
import com.zorvyn.finance.entity.Role;
import com.zorvyn.finance.entity.UserStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final List<UserDTO> users = new ArrayList<>();

    // Dummy data (for testing)
    public UserService() {
        UserDTO user = new UserDTO();
        user.setId(1L);
        user.setName("Kunal");
        user.setEmail("kunal@test.com");
        user.setRole(Role.USER);
        user.setStatus(UserStatus.ACTIVE);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        users.add(user);
    }

    public List<UserDTO> getAllUsers() {
        return users;
    }

    public UserDTO getUserById(Long id) {
        return users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}