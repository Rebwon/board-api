package com.rebwon.demosecurityboard.modules.account.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.rebwon.demosecurityboard.modules.account.domain.Account;
import com.rebwon.demosecurityboard.modules.account.web.payload.SignUpPayload;

public interface AccountService extends UserDetailsService {
	Account register(SignUpPayload payload);
}
