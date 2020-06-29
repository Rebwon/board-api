package com.rebwon.demosecurityboard.modules.activity.domain;

import java.util.List;

import com.rebwon.demosecurityboard.modules.account.domain.Account;

public class Activities {
	private final List<Activity> activities;

	public Activities(List<Activity> activities) {
		this.activities = activities;
	}

	public Integer calculateTotalScore(Account account) {
		return this.activities.stream()
			.filter(a -> a.getAccountId().equals(account.getId()))
			.mapToInt(Activity::getScore)
			.sum();
	}
}
