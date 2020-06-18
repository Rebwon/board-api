package com.rebwon.demosecurityboard.modules.post.service;

import com.rebwon.demosecurityboard.modules.account.domain.Account;
import com.rebwon.demosecurityboard.modules.post.domain.Post;
import com.rebwon.demosecurityboard.modules.post.api.payload.PostCreatePayload;

public interface PostService {
	Post createPost(PostCreatePayload payload, Account account);
}
