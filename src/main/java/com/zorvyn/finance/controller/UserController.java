package com.zorvyn.finance.controller;

import com.zorvyn.finance.dto.ChangePasswordDTO;
import com.zorvyn.finance.dto.UserDTO;
import com.zorvyn.finance.dto.UserUpdateDTO;
import com.zorvyn.finance.entity.Role;
import com.zorvyn.finance.entity.UserStatus;
import com.zorvyn.finance.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@PreAuthorize("isAuthenticated()")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Get all users (admin only)
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Get current user's profile
     */
    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getMyProfile(Authentication authentication) {
        String userId = authentication.getPrincipal().toString();
        UserDTO user = userService.getUserById(Long.valueOf(userId));
        return ResponseEntity.ok(user);
    }

    /**
     * Get user by ID (admin only)
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    /**
     * Get users by role (admin only)
     */
    @GetMapping("/role/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getUsersByRole(@PathVariable Role role) {
        List<UserDTO> users = userService.getUsersByRole(role);
        return ResponseEntity.ok(users);
    }

    /**
     * Get users by status (admin only)
     */
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getUsersByStatus(@PathVariable UserStatus status) {
        List<UserDTO> users = userService.getUsersByStatus(status);
        return ResponseEntity.ok(users);
    }

    /**
     * Update current user's profile
     */
    @PutMapping("/profile")
    public ResponseEntity<UserDTO> updateProfile(
            @Valid @RequestBody UserUpdateDTO updateDTO,
            Authentication authentication) {
        String userId = authentication.getPrincipal().toString();
        UserDTO updated = userService.updateUser(Long.valueOf(userId), updateDTO.getName(), updateDTO.getEmail());
        return ResponseEntity.ok(updated);
    }

    /**
     * Change password
     */
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @Valid @RequestBody ChangePasswordDTO changePasswordDTO,
            Authentication authentication) {
        
        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("New password and confirm password do not match");
        }

        String userId = authentication.getPrincipal().toString();
        userService.changePassword(Long.valueOf(userId), changePasswordDTO.getOldPassword(), changePasswordDTO.getNewPassword());
        return ResponseEntity.ok("Password changed successfully");
    }

    /**
     * Update user role (admin only)
     */
    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updateUserRole(
            @PathVariable Long id,
            @RequestParam Role role) {
        UserDTO updated = userService.updateUserRole(id, role);
        return ResponseEntity.ok(updated);
    }

    /**
     * Update user status (admin only)
     */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updateUserStatus(
            @PathVariable Long id,
            @RequestParam UserStatus status) {
        UserDTO updated = userService.updateUserStatus(id, status);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete user (admin only)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}
