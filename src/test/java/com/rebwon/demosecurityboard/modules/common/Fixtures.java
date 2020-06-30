package com.rebwon.demosecurityboard.modules.common;

import java.util.List;

import com.rebwon.demosecurityboard.modules.account.domain.Account;
import com.rebwon.demosecurityboard.modules.post.domain.Post;
import com.rebwon.demosecurityboard.modules.post.domain.Tag;

public class Fixtures {
	public static Post generatePost(Account account) {
		return Post.of("The Auth Account", "Auth contents", account, "Auth",
			List.of(new Tag("Spring"), new Tag("Hibernate")));
	}
}
