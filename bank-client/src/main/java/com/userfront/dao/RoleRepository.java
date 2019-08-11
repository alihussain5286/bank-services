package com.userfront.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.utility.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    
    Role findByName(String name);
}
