package com.userfront.service;

import java.util.Set;

import com.example.utility.entity.User;
import com.example.utility.entity.UserRole;
import com.example.utility.exception.ApiException;

public interface UserService {
	
    User findByUsername(String username);

    User findByEmail(String email);

    boolean checkUserExists(String username, String email);

    boolean checkUsernameExists(String username);

    boolean checkEmailExists(String email);
    
    User createUser(User user, Set<UserRole> userRoles)throws ApiException;
    
    User saveUser (User user); 
}
