package com.userfront.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.utility.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
	User findByUsername(String username);
	
    User findByEmail(String email);
    
    List<User> findAll();
}
