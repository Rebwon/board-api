package com.rebwon.demosecurityboard.modules.account.domain;

import static java.lang.Integer.*;

import com.rebwon.demosecurityboard.modules.account.domain.activity.ActivityCondition;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString @Getter
public class ActivityScore {
	public static final ActivityScore ZERO = ActivityScore.valueOf(0);

	private final Integer score;

	ActivityScore(Integer score) {
		this.score = score;
	}

	public static ActivityScore valueOf(int score) {
		return new ActivityScore(score);
	}

	public ActivityScore increase(ActivityCondition condition) {
		return new ActivityScore(sum(this.score, condition.getScore()));
	}

	public ActivityScore decrease(ActivityCondition condition) {
		return new ActivityScore(minus(this.score, condition.getScore()));
	}

	private Integer minus(int a, int b) {
		return a-b;
	}
}
