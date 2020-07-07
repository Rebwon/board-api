package com.rebwon.demosecurityboard.modules.account.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.rebwon.demosecurityboard.modules.account.domain.Account;
import com.rebwon.demosecurityboard.modules.account.api.payload.AccountUpdatePayload;
import com.rebwon.demosecurityboard.modules.account.api.payload.SignUpPayload;

public interface AccountService extends UserDetailsService {
	Account register(SignUpPayload payload);

	Account findAccount(Long id, Account account);

	Account update(Long id, Account account, AccountUpdatePayload payload);

	UserDetails loadUserById(Long id);
}
