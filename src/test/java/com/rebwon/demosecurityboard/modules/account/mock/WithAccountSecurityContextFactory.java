package com.rebwon.demosecurityboard.modules.account.mock;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import com.rebwon.demosecurityboard.modules.account.service.AccountService;
import com.rebwon.demosecurityboard.modules.account.api.payload.SignUpPayload;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WithAccountSecurityContextFactory implements WithSecurityContextFactory<WithAccount> {
	private final AccountService accountService;

	@Override
	public SecurityContext createSecurityContext(WithAccount withAccount) {
		String nickname = withAccount.value();

		SignUpPayload payload = new SignUpPayload();
		payload.setNickname(nickname);
		payload.setEmail(nickname + "@gmail.com");
		payload.setPassword("password!");
		accountService.register(payload);

		UserDetails principal = accountService.loadUserByUsername(nickname);
		Authentication authentication = new UsernamePasswordAuthenticationToken(
			principal, principal.getPassword(), principal.getAuthorities());
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(authentication);
		return context;
	}
}
