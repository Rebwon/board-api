package com.rebwon.demosecurityboard.modules.account.api.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.rebwon.demosecurityboard.modules.account.domain.AccountRepository;
import com.rebwon.demosecurityboard.modules.account.api.payload.AccountUpdatePayload;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AccountUpdateValidator {
	private final AccountRepository accountRepository;

	public void validate(AccountUpdatePayload payload, Errors errors) {
		if(accountRepository.existsByNickname(payload.getNickname())) {
			errors.rejectValue("nickname", "invalid.nickname",
				new Object[]{payload.getNickname()}, "This nickname has using in application");
		}
		if(isPasswordMissMatched(payload)) {
			errors.rejectValue("newPassword", "wrong.value", "Input new password is miss matched");
		}
	}

	private boolean isPasswordMissMatched(AccountUpdatePayload payload) {
		return !payload.getNewPassword().equals(payload.getNewPasswordConfirm());
	}
}
