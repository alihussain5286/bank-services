package com.userfront.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.utility.EncryptUtil;
import com.example.utility.entity.PrimaryAccount;
import com.example.utility.entity.PrimaryTransaction;
import com.example.utility.entity.SavingsAccount;
import com.example.utility.entity.SavingsTransaction;
import com.example.utility.entity.User;
import com.example.utility.exception.ApiException;
import com.example.utility.model.TransactionRequest;
import com.example.utility.validation.ValidationGroups.CheckAmountValidation;
import com.example.utility.validation.ValidationGroups.GeneralValidation;
import com.userfront.service.AccountService;
import com.userfront.service.TransactionService;
import com.userfront.service.UserService;
import com.userfront.util.CheckAmountRequest;

@Controller
@RequestMapping("/account")
public class AccountController {

	@Autowired
	private UserService userService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private TransactionService transactionService;

	@GetMapping("/primaryAccount")
	public String primaryAccount(Model model, Principal principal) throws ApiException {
		List<PrimaryTransaction> primaryTransactionList = transactionService.findPrimaryTransactionList(principal.getName());
		User user = userService.findByUsername(principal.getName());
		PrimaryAccount primaryAccount = user.getPrimaryAccount();
		model.addAttribute("primaryAccount", primaryAccount);
		model.addAttribute("primaryTransactionList", primaryTransactionList);
		return "primaryAccount";
	}

	@GetMapping("/savingsAccount")
	public String savingsAccount(Model model, Principal principal) throws ApiException {
		List<SavingsTransaction> savingsTransactionList = transactionService.findSavingsTransactionList(principal.getName());
		User user = userService.findByUsername(principal.getName());
		SavingsAccount savingsAccount = user.getSavingsAccount();
		model.addAttribute("savingsAccount", savingsAccount);
		model.addAttribute("savingsTransactionList", savingsTransactionList);
		return "savingsAccount";
	}

	@GetMapping("/deposit")
	public String deposit(Model model) {
		model.addAttribute("transactionRequest", new TransactionRequest());
		return "deposit";
	}

	@PostMapping("/deposit")
	public String depositPOST(@ModelAttribute("transactionRequest")  @Validated(GeneralValidation.class) TransactionRequest transactionRequest,BindingResult bindingResult ,Principal principal) throws ApiException {
		if(bindingResult.hasErrors()) {
			return "deposit";
		}
		accountService.deposit(transactionRequest, principal.getName());
		return "redirect:/userFront";
	}

	@GetMapping(value = "/withdraw")
	public String withdraw(Model model,Principal principal) {
		CheckAmountRequest checkAmountRequest= new CheckAmountRequest();
		checkAmountRequest.setUserName(EncryptUtil.encrypt(principal.getName()));
		model.addAttribute("transactionRequest",checkAmountRequest);
		return "withdraw";
	}

	@PostMapping(value = "/withdraw")
	public String withdrawPOST(@ModelAttribute("transactionRequest")  @Validated(CheckAmountValidation.class) CheckAmountRequest checkAmountRequest, BindingResult bindingResult,Principal principal) throws ApiException {
		if(bindingResult.hasErrors()) {
			return "withdraw";
		}
		accountService.withdraw(checkAmountRequest, principal.getName());
		return "redirect:/userFront";
	}
}
