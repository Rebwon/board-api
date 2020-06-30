package com.rebwon.demosecurityboard.modules.post.domain.event;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.rebwon.demosecurityboard.modules.account.domain.AccountValidator;
import com.rebwon.demosecurityboard.modules.activity.service.ActivityService;
import lombok.RequiredArgsConstructor;

@Async
@Component
@Transactional
@RequiredArgsConstructor
public class PostEventHandler {
	private final ActivityService activityService;
	private final AccountValidator accountValidator;

	@EventListener
	public void handlePostCreatedEvent(PostCreatedEvent createdEvent) {
		activityService.writePost(createdEvent);
		accountValidator.validateTotalScore(createdEvent.getWriterId());
	}

	@EventListener
	public void handlePostDeletedEvent(PostDeletedEvent deletedEvent) {
		activityService.deletePost(deletedEvent);
		accountValidator.validateTotalScore(deletedEvent.getWriterId());
	}
}
