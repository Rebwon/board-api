package com.rebwon.demosecurityboard.modules.post.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rebwon.demosecurityboard.modules.account.domain.Account;
import com.rebwon.demosecurityboard.modules.account.domain.AccountRepository;
import com.rebwon.demosecurityboard.modules.post.domain.Post;
import com.rebwon.demosecurityboard.modules.post.domain.PostRepository;
import com.rebwon.demosecurityboard.modules.post.domain.Tag;
import com.rebwon.demosecurityboard.modules.post.web.payload.PostCreatePayload;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
	private final PostRepository postRepository;
	private final AccountRepository accountRepository;

	@Override
	public Post createPost(PostCreatePayload payload, Account account) {
		Account writer = accountRepository.findById(account.getId()).orElseThrow(IllegalArgumentException::new);
		List<Tag> tags = getTags(payload.getTagName());
		Post post = Post.of(payload.getTitle(), payload.getContent(), writer, payload.getCategoryName(), tags);
		return this.postRepository.save(post);
	}

	private List<Tag> getTags(List<String> tagName) {
		if(tagName == null)
			return Collections.emptyList();
		return tagName.stream().map(Tag::new).collect(Collectors.toList());
	}
}
