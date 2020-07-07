package com.rebwon.demosecurityboard.modules.post.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rebwon.demosecurityboard.modules.account.api.exception.AccountNotFoundException;
import com.rebwon.demosecurityboard.modules.account.domain.Account;
import com.rebwon.demosecurityboard.modules.account.domain.AccountRepository;
import com.rebwon.demosecurityboard.modules.comment.domain.Comment;
import com.rebwon.demosecurityboard.modules.comment.domain.CommentRepository;
import com.rebwon.demosecurityboard.modules.post.api.exception.InvalidWriterException;
import com.rebwon.demosecurityboard.modules.post.api.exception.PostNotFoundException;
import com.rebwon.demosecurityboard.modules.post.api.payload.PostUpdatePayload;
import com.rebwon.demosecurityboard.modules.post.domain.Post;
import com.rebwon.demosecurityboard.modules.post.domain.PostRepository;
import com.rebwon.demosecurityboard.modules.post.domain.Tag;
import com.rebwon.demosecurityboard.modules.post.api.payload.PostCreatePayload;
import com.rebwon.demosecurityboard.modules.post.domain.event.PostCreatedEvent;
import com.rebwon.demosecurityboard.modules.post.domain.event.PostDeletedEvent;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
	private final PostRepository postRepository;
	private final CommentRepository commentRepository;
	private final AccountRepository accountRepository;
	private final ApplicationEventPublisher publisher;

	@Override
	public Post create(PostCreatePayload payload, Account account) {
		Account writer = accountRepository.findById(account.getId()).orElseThrow(AccountNotFoundException::new);
		List<Tag> tags = hasEmptyOrConvert(payload.getTagName());
		Post post = this.postRepository
			.save(Post.of(payload.getTitle(), payload.getContent(), writer, payload.getCategoryName(), tags));
		publisher.publishEvent(new PostCreatedEvent(post));
		return post;
	}

	private List<Tag> hasEmptyOrConvert(List<String> tagName) {
		if(tagName == null)
			return Collections.emptyList();
		return tagName.stream().map(Tag::new).collect(Collectors.toList());
	}

	@Override
	public Post update(Long id, Account account, PostUpdatePayload payload) {
		Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
		List<Tag> tags = hasEmptyOrConvert(payload.getTagName());
		if(!post.isSameWriter(account)) {
			throw new InvalidWriterException(account.getNickname());
		}
		post.update(payload.getTitle(), payload.getContent(), payload.getCategoryName(), tags);
		return post;
	}

	@Transactional(readOnly = true)
	@Override
	public Post findOne(Long postId) {
		return postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
	}

	@Override
	public void delete(Long postId, Account account) {
		Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
		List<Comment> comments = commentRepository.findByPost_Id(post.getId());
		if(post.isSameWriter(account) && comments.isEmpty())
			publisher.publishEvent(new PostDeletedEvent(post));
			postRepository.delete(post);
	}
}
