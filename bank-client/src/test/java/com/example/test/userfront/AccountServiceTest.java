package com.example.test.userfront;

import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;

import com.example.utility.Constant;
import com.example.utility.ErrorConstant;
import com.example.utility.entity.PrimaryAccount;
import com.example.utility.entity.SavingsAccount;
import com.example.utility.entity.User;
import com.example.utility.exception.ApiException;
import com.example.utility.exception.ApiExceptionMatcher;
import com.example.utility.model.AccountType;
import com.example.utility.model.Request;
import com.example.utility.model.RequestType;
import com.example.utility.model.Response;
import com.example.utility.model.TransactionRequest;
import com.userfront.service.RestService;
import com.userfront.service.UserService;
import com.userfront.service.impl.AccountServiceImpl;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AccountServiceTest {

	@Mock
	private UserService userService;

	@Mock
	private RestService restService;

	@InjectMocks
	private AccountServiceImpl accountService;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void depositOrWithdrawInvalidAccountType() throws ApiException {
		thrown.expect(new ApiExceptionMatcher(ErrorConstant.INVALID_ACCOUNT_TYPE));
		String userName="ali";
		User user = new User();
		TransactionRequest transactioRequest= new TransactionRequest();
		Request request = new Request();
		user.setPrimaryAccount(new PrimaryAccount());
		user.setSavingsAccount(new SavingsAccount());
		request.setTransactionRequest(transactioRequest);
		Response response = new Response();
		response.setStatus(Constant.SUCCESS);
		when(userService.findByUsername(userName)).thenReturn(user);
		when(restService.getRestResponse( any(Request.class),  eq(RequestType.DEFAULT), eq(HttpMethod.POST), any(String.class))).thenReturn(response);
		accountService.deposit(transactioRequest, userName);
	}

	@Test
	public void depositOrWithdrawWhenUserDoesnotExists() throws ApiException {
		thrown.expect(new ApiExceptionMatcher(ErrorConstant.USER_DOES_NOT_EXITS));
		String userName="ali";
		User user = new User();
		TransactionRequest transactioRequest= new TransactionRequest();
		Request request = new Request();
		user.setPrimaryAccount(new PrimaryAccount());
		user.setSavingsAccount(new SavingsAccount());
		request.setTransactionRequest(transactioRequest);
		Response response = new Response();
		response.setStatus(Constant.SUCCESS);
		when(userService.findByUsername(userName)).thenReturn(null);
		accountService.deposit(transactioRequest, userName);
	}


	@Test
	public void depositOrWithdrawSuccess() throws ApiException {
		String userName="ali";
		User user = new User();
		TransactionRequest transactioRequest= new TransactionRequest();
		transactioRequest.setAccountType(AccountType.PRIMARY.name());
		Request request = new Request();
		user.setPrimaryAccount(new PrimaryAccount());
		user.setSavingsAccount(new SavingsAccount());
		request.setTransactionRequest(transactioRequest);
		Response response = new Response();
		response.setStatus(Constant.SUCCESS);
		when(userService.findByUsername(userName)).thenReturn(user);
		when(restService.getRestResponse( any(Request.class),  eq(RequestType.DEFAULT), eq(HttpMethod.POST), any(String.class))).thenReturn(response);
		assertSame(Constant.SUCCESS,accountService.deposit(transactioRequest, userName).getStatus());
	}
}

