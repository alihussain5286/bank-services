package com.userfront.service.impl;

import java.util.Set;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.utility.ErrorConstant;
import com.example.utility.entity.User;
import com.example.utility.entity.UserRole;
import com.example.utility.exception.ApiException;
import com.example.utility.model.Response;
import com.userfront.dao.RoleRepository;
import com.userfront.dao.UserRepository;
import com.userfront.service.AccountService;
import com.userfront.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private AccountService accountService;

	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public User createUser(User user, Set<UserRole> userRoles)throws ApiException {
		logger.info("<-------Enter createUser------------------>");
		User localUser= null;
		try {
			localUser = userRepository.findByUsername(user.getUsername());
			if (localUser != null) {
				logger.info("User with username {} already exist. Nothing will be done. ", user.getUsername());
			} else {
				String encryptedPassword = passwordEncoder.encode(user.getPassword());
				user.setPassword(encryptedPassword);
				for (UserRole ur : userRoles) {
					roleRepository.save(ur.getRole());
				}
				user.getUserRoles().addAll(userRoles);
				Response response=accountService.createAccount();
				user.setPrimaryAccount(response.getPrimaryAccount());
				user.setSavingsAccount(response.getSavingsAccount());
				localUser = userRepository.save(user);
				logger.info("<-------Exit createUser------------------>");
			}
		}catch(Exception e) {
			logger.error("Exception in createUser::{}",e);
			throw new ApiException(ErrorConstant.SERVICE_EXCEPTION);
		}
		return localUser;
	}

	@Override
	public boolean checkUserExists(String username, String email){
		if (checkUsernameExists(username) || checkEmailExists(username)) {
			return true;
		} else {
			return false;
		}
	}
	@Override
	public boolean checkUsernameExists(String username) {
		if (null != findByUsername(username)) {
			return true;
		}

		return false;
	}
	@Override
	public boolean checkEmailExists(String email) {
		if (null != findByEmail(email)) {
			return true;
		}
		return false;
	}
	@Override
	public User saveUser (User user) {
		return userRepository.save(user);
	}

}
