package com.zorvyn.finance.service;

import com.zorvyn.finance.dto.UserDTO;
import com.zorvyn.finance.entity.Role;
import com.zorvyn.finance.entity.User;
import com.zorvyn.finance.entity.UserStatus;
import com.zorvyn.finance.exception.ResourceNotFoundException;
import com.zorvyn.finance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Get all users (admin only)
     */
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
            .stream()
            .map(this::mapToDTO)
            .toList();
    }

    /**
     * Get user by ID
     */
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return mapToDTO(user);
    }

    /**
     * Get user by email
     */
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return mapToDTO(user);
    }

    /**
     * Get users by role
     */
    public List<UserDTO> getUsersByRole(Role role) {
        return userRepository.findByRole(role)
            .stream()
            .map(this::mapToDTO)
            .toList();
    }

    /**
     * Get users by status
     */
    public List<UserDTO> getUsersByStatus(UserStatus status) {
        return userRepository.findByStatus(status)
            .stream()
            .map(this::mapToDTO)
            .toList();
    }

    /**
     * Update user name and email
     */
    public UserDTO updateUser(Long id, String name, String email) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        
        user.setName(name);
        user.setEmail(email);
        user.setUpdatedAt(LocalDateTime.now());
        
        User updated = userRepository.save(user);
        return mapToDTO(updated);
    }

    /**
     * Update user role (admin only)
     */
    public UserDTO updateUserRole(Long id, Role role) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        
        user.setRole(role);
        user.setUpdatedAt(LocalDateTime.now());
        
        User updated = userRepository.save(user);
        return mapToDTO(updated);
    }

    /**
     * Update user status (admin only)
     */
    public UserDTO updateUserStatus(Long id, UserStatus status) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        
        user.setStatus(status);
        user.setUpdatedAt(LocalDateTime.now());
        
        User updated = userRepository.save(user);
        return mapToDTO(updated);
    }

    /**
     * Change user password
     */
    public void changePassword(Long id, String oldPassword, String newPassword) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    /**
     * Delete user (admin only)
     */
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        userRepository.delete(user);
    }

    /**
     * Helper method to convert User entity to DTO
     */
    private UserDTO mapToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setStatus(user.getStatus());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }
}
