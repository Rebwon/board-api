package com.rebwon.demosecurityboard.modules.account.api;

import javax.validation.Valid;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rebwon.demosecurityboard.modules.account.api.exception.NotOwnerException;
import com.rebwon.demosecurityboard.modules.account.domain.Account;
import com.rebwon.demosecurityboard.modules.account.api.response.AccountResponse;
import com.rebwon.demosecurityboard.modules.account.domain.AccountRepository;
import com.rebwon.demosecurityboard.modules.account.domain.AuthAccount;
import com.rebwon.demosecurityboard.modules.account.service.AccountService;
import com.rebwon.demosecurityboard.modules.account.api.payload.AccountUpdatePayload;
import com.rebwon.demosecurityboard.modules.account.api.payload.SignUpPayload;
import com.rebwon.demosecurityboard.modules.account.api.validator.AccountUpdateValidator;
import com.rebwon.demosecurityboard.modules.account.api.validator.SignUpPayloadValidator;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/accounts")
@RequiredArgsConstructor
public class AccountController {
	private final AccountService accountService;
	private final AccountRepository accountRepository;
	private final SignUpPayloadValidator signUpPayloadValidator;
	private final AccountUpdateValidator accountUpdateValidator;

	@PostMapping
	public ResponseEntity<EntityModel<Account>> register(@RequestBody @Valid SignUpPayload payload) {
		signUpPayloadValidator.validate(payload);
		Account newAccount = accountService.register(payload);
		AccountResponse response = AccountResponse.of(newAccount,
			Link.of("/docs/index.html#resources-accounts-create").withRel("profile"));
		return ResponseEntity.created(response.getUri()).body(response.getModel());
	}

	@GetMapping("/{id}")
	public ResponseEntity<EntityModel<Account>> getAccount(@PathVariable Long id, @AuthAccount Account account) {
		if(account.isNowOwner(id)) {
			throw new NotOwnerException(account.getNickname());
		}
		Account dbAccount = accountRepository.findById(id).orElseThrow(IllegalArgumentException::new);
		AccountResponse response = AccountResponse.of(dbAccount,
			Link.of("/docs/index.html#resources-accounts-get").withRel("profile"));
		return ResponseEntity.ok(response.getModel());
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> updateAccount(@PathVariable Long id, @RequestBody @Valid AccountUpdatePayload payload,
		@AuthAccount Account account) {
		accountUpdateValidator.validate(payload);
		if(account.isNowOwner(id)) {
			throw new NotOwnerException(account.getNickname());
		}
		accountService.update(account, payload);
		return ResponseEntity.noContent().build();
	}
}
