package com.example.expense_tracker.repository;

import com.example.expense_tracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository
        extends JpaRepository<User, Long> {

    User findByEmail(String email);

    // ACTIVE USERS

    @Query("""
            SELECT COUNT(u)
            FROM User u
            WHERE u.status='ACTIVE'
            """)
    Long countActiveUsers();

    // INACTIVE USERS

    @Query("""
            SELECT COUNT(u)
            FROM User u
            WHERE u.status='INACTIVE'
            """)
    Long countInactiveUsers();
}