package com.rebwon.demosecurityboard.modules.account.web.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.rebwon.demosecurityboard.modules.account.domain.AccountRepository;
import com.rebwon.demosecurityboard.modules.account.web.payload.SignUpPayload;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SignUpPayloadValidator implements Validator {
	private final AccountRepository accountRepository;

	@Override
	public boolean supports(Class<?> aClass) {
		return aClass.isAssignableFrom(SignUpPayload.class);
	}

	@Override
	public void validate(Object object, Errors errors) {
		SignUpPayload signUpPayload = (SignUpPayload) object;
		if (accountRepository.existsByEmail(signUpPayload.getEmail())) {
			errors.rejectValue("email", "invalid.email",
				new Object[]{signUpPayload.getEmail()}, "This Email has using in application!");
		}
		if (accountRepository.existsByNickname(signUpPayload.getNickname())) {
			errors.rejectValue("nickname", "invalid.nickname",
				new Object[]{signUpPayload.getEmail()}, "This nickname has using in application!");
		}
	}
}
