package com.rebwon.demosecurityboard.modules.activity.domain;

import static com.rebwon.demosecurityboard.modules.activity.domain.QActivity.*;

import javax.persistence.EntityManager;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.rebwon.demosecurityboard.modules.activity.exception.ActivityNotFoundException;

public class ActivityRepositoryImpl implements ActivityRepositoryCustom{
	private final JPAQueryFactory factory;

	public ActivityRepositoryImpl(EntityManager em) {
		this.factory = new JPAQueryFactory(em);
	}

	@Override
	public void deleteActivity(Long accountId, Long postId) {
		long execute = factory.delete(activity)
			.where(activity.accountId.eq(accountId))
			.where(activity.postId.eq(postId))
			.execute();
		if(execute == 0) throw new ActivityNotFoundException();
	}
}
