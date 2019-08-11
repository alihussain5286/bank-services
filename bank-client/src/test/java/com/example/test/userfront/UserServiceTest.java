package com.example.test.userfront;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.utility.entity.PrimaryAccount;
import com.example.utility.entity.Role;
import com.example.utility.entity.SavingsAccount;
import com.example.utility.entity.User;
import com.example.utility.entity.UserRole;
import com.example.utility.model.Response;
import com.userfront.dao.RoleRepository;
import com.userfront.dao.UserRepository;
import com.userfront.service.AccountService;
import com.userfront.service.impl.UserSecurityService;
import com.userfront.service.impl.UserServiceImpl;

@RunWith(MockitoJUnitRunner.Silent.class)
public class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private AccountService accountService;

	@Mock
	private RoleRepository roleRepository;

	@Mock
	private BCryptPasswordEncoder passwordEncoder;

	@InjectMocks
	private UserServiceImpl userServiceImpl;

	@InjectMocks
	private UserSecurityService userSecurityServiceImpl;

	@Test
	public void createUserSuccessfully() throws Exception {
		User user= new User();
		user.setFirstName("ali");
		user.setUsername("ali");
		user.setPassword("ali");
		Role role = new Role();
		role.setName("ROLE_USER");
		Set<UserRole> userRole = new HashSet<>();
		userRole.add(new UserRole(user, role));
		Response reponse = new Response();
		reponse.setPrimaryAccount(new PrimaryAccount());
		reponse.setSavingsAccount(new SavingsAccount());
		when(roleRepository.save(new Role())).thenReturn(null);
		when(accountService.createAccount()).thenReturn(reponse);
		when(userRepository.save(user)).thenReturn(user);
		when(userServiceImpl.findByUsername("ali")).thenReturn(null);
		when(passwordEncoder.encode("ali")).thenReturn("zswqe12462=Xasd");
		assertSame("ali", userServiceImpl.createUser(user, userRole).getUsername());
	}

	@Test(expected = UsernameNotFoundException.class)
	public void userNotExistsError() {
		String userName="ali";
		when(userServiceImpl.findByUsername(userName)).thenReturn(null);
		userSecurityServiceImpl.loadUserByUsername(userName);
	}
}

