package com.rebwon.demosecurityboard.modules.post.domain.event;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.rebwon.demosecurityboard.modules.activity.domain.Activity;
import com.rebwon.demosecurityboard.modules.activity.domain.ActivityRepository;
import com.rebwon.demosecurityboard.modules.activity.domain.PostScoreCondition;
import lombok.RequiredArgsConstructor;

@Async
@Component
@Transactional
@RequiredArgsConstructor
public class PostEventHandler {
	private final ActivityRepository activityRepository;

	@EventListener
	public void handlePostCreatedEvent(PostCreatedEvent createdEvent) {
		Activity activity = Activity.writePost(createdEvent.getWriterId(), createdEvent.getPostId(),
			new PostScoreCondition());
		activityRepository.save(activity);
	}

	@EventListener
	public void handlePostDeletedEvent(PostDeletedEvent deletedEvent) {
		activityRepository.deleteActivityByAccountIdEqualsAndPostIdEquals(deletedEvent.getWriterId(), deletedEvent.getPostId());
	}
}
