package com.zorvyn.finance.repository;

import com.zorvyn.finance.entity.Role;
import com.zorvyn.finance.entity.User;
import com.zorvyn.finance.entity.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
        
    // Find user by email
    Optional<User> findByEmail(String email);

    // Find users by status
    List<User> findByStatus(UserStatus status);

    // Find users by role
    List<User> findByRole(Role role);

    // Find all users (inherited from JpaRepository)
    List<User> findAll();
}