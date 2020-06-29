package com.rebwon.demosecurityboard.modules.account.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rebwon.demosecurityboard.modules.account.api.exception.AccountNotFoundException;
import com.rebwon.demosecurityboard.modules.account.domain.Account;
import com.rebwon.demosecurityboard.modules.account.domain.AccountRepository;
import com.rebwon.demosecurityboard.modules.account.domain.UserAccount;
import com.rebwon.demosecurityboard.modules.account.api.payload.AccountUpdatePayload;
import com.rebwon.demosecurityboard.modules.account.api.payload.SignUpPayload;
import com.rebwon.demosecurityboard.modules.activity.domain.Activities;
import com.rebwon.demosecurityboard.modules.activity.domain.ActivityRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
	private final AccountRepository accountRepository;
	private final ActivityRepository activityRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional(readOnly = true)
	@Override
	public UserDetails loadUserByUsername(String emailOrNickname) throws UsernameNotFoundException {
		Account account = accountRepository.findByEmail(emailOrNickname);
		if(account == null) {
			account = accountRepository.findByNickname(emailOrNickname);
		}

		if(account == null) {
			throw new UsernameNotFoundException(emailOrNickname);
		}
		return new UserAccount(account);
	}

	@Override
	public Account register(SignUpPayload payload) {
		Account account = Account.of(payload.getEmail(),
			this.passwordEncoder.encode(payload.getPassword()), payload.getNickname());
		return this.accountRepository.save(account);
	}

	@Transactional(readOnly = true)
	@Override
	public Account findAccount(Long id, Account authAccount) {
		authAccount.isNowOwner(id);
		Activities activities = new Activities(activityRepository.findByAccountId(id));
		Account account = accountRepository.findById(id).orElseThrow(AccountNotFoundException::new);
		account.calculateTotalScore(activities);
		return account;
	}

	@Override
	public Account update(Long id, Account authAccount, AccountUpdatePayload payload) {
		authAccount.isNowOwner(id);
		authAccount.update(payload.getNickname(), this.passwordEncoder.encode(payload.getNewPassword()));
		return authAccount;
	}
}
