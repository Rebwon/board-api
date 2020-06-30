package com.rebwon.demosecurityboard.modules.account.domain;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.rebwon.demosecurityboard.modules.account.api.exception.AccountNotFoundException;
import com.rebwon.demosecurityboard.modules.activity.domain.Activities;
import com.rebwon.demosecurityboard.modules.activity.domain.ActivityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class AccountValidator {
	private final AccountRepository accountRepository;
	private final ActivityRepository activityRepository;

	public void validateTotalScore(Long accountId) {
		Account account = accountRepository.findById(accountId)
			.orElseThrow(AccountNotFoundException::new);
		Activities activities = new Activities(activityRepository.findByAccountId(accountId));
		account.calculateTotalScore(activities);
	}
}
