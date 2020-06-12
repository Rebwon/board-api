package com.rebwon.demosecurityboard.modules.account.web;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rebwon.demosecurityboard.modules.account.domain.Account;
import com.rebwon.demosecurityboard.modules.account.service.AccountService;
import com.rebwon.demosecurityboard.modules.account.web.payload.SignUpPayload;
import com.rebwon.demosecurityboard.modules.account.web.validator.SignUpPayloadValidator;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AccountController {
	private final AccountService accountService;
	private final SignUpPayloadValidator signUpPayloadValidator;

	@InitBinder("signUpPayload")
	public void initBinder(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(signUpPayloadValidator);
	}

	@PostMapping("/signup")
	private ResponseEntity register(@RequestBody @Valid SignUpPayload payload, Errors errors) {
		if(errors.hasErrors()) {
			return ResponseEntity.badRequest().body(errors);
		}
		Account account = accountService.register(payload);
		return ResponseEntity.ok("Register Success user:" + account.getNickname());
	}
}
