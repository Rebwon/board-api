package com.rebwon.demosecurityboard.modules.account.api.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.rebwon.demosecurityboard.modules.account.api.exception.NicknameDuplicateException;
import com.rebwon.demosecurityboard.modules.account.api.exception.PasswordMissMatchException;
import com.rebwon.demosecurityboard.modules.account.domain.AccountRepository;
import com.rebwon.demosecurityboard.modules.account.api.payload.AccountUpdatePayload;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AccountUpdateValidator {
	private final AccountRepository accountRepository;

	public void validate(AccountUpdatePayload payload) {
		if(accountRepository.existsByNickname(payload.getNickname())) {
			throw new NicknameDuplicateException(payload.getNickname());
		}
		if(isPasswordMissMatched(payload)) {
			throw new PasswordMissMatchException();
		}
	}

	private boolean isPasswordMissMatched(AccountUpdatePayload payload) {
		return !payload.getNewPassword().equals(payload.getNewPasswordConfirm());
	}
}
