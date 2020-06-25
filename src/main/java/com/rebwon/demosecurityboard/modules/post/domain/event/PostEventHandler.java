package com.rebwon.demosecurityboard.modules.post.domain.event;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.rebwon.demosecurityboard.modules.account.api.exception.AccountNotFoundException;
import com.rebwon.demosecurityboard.modules.account.domain.Account;
import com.rebwon.demosecurityboard.modules.account.domain.AccountRepository;
import com.rebwon.demosecurityboard.modules.account.domain.activity.PostActivityCondition;
import lombok.RequiredArgsConstructor;

@Async
@Component
@Transactional
@RequiredArgsConstructor
public class PostEventHandler {
	private final AccountRepository accountRepository;

	@EventListener
	public void handlePostCreatedEvent(PostCreatedEvent createdEvent) {
		Account account = accountRepository.findById(createdEvent.getWriterId())
			.orElseThrow(AccountNotFoundException::new);
		account.increaseActivityScore(new PostActivityCondition());
	}

	@EventListener
	public void handlePostDeletedEvent(PostDeletedEvent deletedEvent) {
		Account account = accountRepository.findById(deletedEvent.getWriterId())
			.orElseThrow(AccountNotFoundException::new);
		account.decreaseActivityScore(new PostActivityCondition());
	}
}
