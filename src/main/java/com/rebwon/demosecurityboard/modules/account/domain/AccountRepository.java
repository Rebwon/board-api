package com.rebwon.demosecurityboard.modules.account.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
	Account findByEmail(String email);

	boolean existsByEmail(String email);

	boolean existsByNickname(String nickname);

	Account findByNickname(String nickname);
}
