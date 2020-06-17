package com.rebwon.demosecurityboard.modules.account.web;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import javax.validation.Valid;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rebwon.demosecurityboard.modules.account.domain.Account;
import com.rebwon.demosecurityboard.modules.account.domain.AccountRepository;
import com.rebwon.demosecurityboard.modules.account.domain.AuthAccount;
import com.rebwon.demosecurityboard.modules.account.service.AccountService;
import com.rebwon.demosecurityboard.modules.account.web.payload.AccountUpdatePayload;
import com.rebwon.demosecurityboard.modules.account.web.payload.SignUpPayload;
import com.rebwon.demosecurityboard.modules.account.web.validator.AccountUpdateValidator;
import com.rebwon.demosecurityboard.modules.account.web.validator.SignUpPayloadValidator;
import com.rebwon.demosecurityboard.modules.index.IndexController;
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
	public ResponseEntity register(@RequestBody @Valid SignUpPayload payload, Errors errors) {
		if(errors.hasErrors()) {
			return badRequest(errors);
		}
		signUpPayloadValidator.validate(payload, errors);
		if(errors.hasErrors()) {
			return badRequest(errors);
		}
		Account newAccount = accountService.register(payload);
		WebMvcLinkBuilder selfLinkBuilder = linkTo(AccountController.class).slash(newAccount.getId());
		EntityModel<Account> model = EntityModel.of(newAccount);
		model.add(selfLinkBuilder.withSelfRel(), selfLinkBuilder.withRel("update-account"),
			Link.of("/docs/index.html#resources-accounts-create").withRel("profile"));
		return ResponseEntity.created(selfLinkBuilder.toUri()).body(model);
	}

	@GetMapping("/{id}")
	public ResponseEntity getAccount(@PathVariable Long id, @AuthAccount Account account) {
		if(isNotOwner(id, account)) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
		Account dbAccount = accountRepository.findById(id).orElseThrow(IllegalArgumentException::new);
		EntityModel<Account> model = EntityModel.of(dbAccount);
		model.add(Link.of("/docs/index.html#resources-accounts-get").withRel("profile"),
			linkTo(AccountController.class).slash(dbAccount.getId()).withSelfRel());

		if(existsAccountAndOwner(id, dbAccount)) {
			model.add(linkTo(AccountController.class).slash(dbAccount.getId()).withRel("update-account"));
		}
		return ResponseEntity.ok(model);
	}

	private boolean existsAccountAndOwner(Long id, Account account) {
		return account != null && account.getId().equals(id);
	}

	@PutMapping("/{id}")
	public ResponseEntity updateAccount(@PathVariable Long id, @RequestBody @Valid AccountUpdatePayload payload,
		@AuthAccount Account account, Errors errors) {
		if(errors.hasErrors()) {
			return badRequest(errors);
		}
		accountUpdateValidator.validate(payload, errors);
		if(errors.hasErrors()) {
			return badRequest(errors);
		}
		if(isNotOwner(id, account)) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
		accountService.update(accountRepository.findById(id).get(), payload);
		return ResponseEntity.noContent().build();
	}

	private boolean isNotOwner(Long id, Account account) {
		return !account.getId().equals(id);
	}

	private ResponseEntity badRequest(Errors errors) {
		return ResponseEntity.badRequest().body(EntityModel.of(errors,
			linkTo(methodOn(IndexController.class).index()).withRel("index")));
	}
}
