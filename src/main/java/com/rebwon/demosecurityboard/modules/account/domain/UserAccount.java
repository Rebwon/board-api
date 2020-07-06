package com.rebwon.demosecurityboard.modules.account.domain;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UserAccount extends User {
	private Account account;

	public UserAccount(Account account) {
		super(account.getEmail(), account.getPassword(), authorities(account.getRoles()));
		this.account = account;
	}

	private static Collection<? extends GrantedAuthority> authorities(Set<AccountRole> roles) {
		return roles.stream()
				.map(r -> new SimpleGrantedAuthority("ROLE" + r.name()))
				.collect(Collectors.toSet());
	}

	public Account getAccount() {
		return account;
	}

	public Long getId() {
		return this.account.getId();
	}
}
