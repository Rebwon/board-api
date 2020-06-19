package com.rebwon.demosecurityboard.modules.account.api.response;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.net.URI;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rebwon.demosecurityboard.modules.account.api.AccountController;
import com.rebwon.demosecurityboard.modules.account.domain.Account;
import lombok.Getter;

@Getter
public class AccountResponse extends EntityModel<Account> {
	private EntityModel<Account> model;
	@JsonIgnore
	private URI uri;

	private AccountResponse(Account account, Link link) {
		WebMvcLinkBuilder selfLinks = linkTo(AccountController.class).slash(account.getId());
		this.model = EntityModel.of(account, selfLinks.withSelfRel(),
			selfLinks.withRel("update-account"), link);
		this.uri = selfLinks.toUri();
	}

	public static AccountResponse of(Account account, Link link) {
		return new AccountResponse(account, link);
	}
}
