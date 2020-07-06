package com.rebwon.demosecurityboard.modules.account.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.DynamicUpdate;

import com.rebwon.demosecurityboard.modules.account.api.exception.NotOwnerException;
import com.rebwon.demosecurityboard.modules.activity.domain.Activities;
import com.rebwon.demosecurityboard.modules.common.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Getter
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = false)
public class Account extends BaseEntity {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String nickname;
	@Column(unique = true, nullable = false)
	private String email;
	private String password;
	private String imageUrl;
	private String providerId;
	@Enumerated(EnumType.STRING)
	private AuthProvider provider;
	@ElementCollection(fetch = FetchType.EAGER)
	@Enumerated(EnumType.STRING)
	private Set<AccountRole> roles;
	private Integer totalScore = 0;

	public static Account of(String email, String password, String nickname) {
		Account account = new Account();
		account.email = email;
		account.password = password;
		account.nickname = nickname;
		account.roles = Set.of(AccountRole.USER);
		account.provider = AuthProvider.LOCAL;
		return account;
	}

	public void update(String nickname, String password) {
		this.nickname = nickname;
		this.password = password;
	}

	public void isNowOwner(Account account) {
		if(!this.equals(account))
			throw new NotOwnerException();
	}

	public void calculateTotalScore(Activities activities) {
		this.totalScore = activities.calculateTotalScore(this);
	}
}
