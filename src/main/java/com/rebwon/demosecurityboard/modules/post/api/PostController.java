package com.rebwon.demosecurityboard.modules.post.api;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rebwon.demosecurityboard.modules.account.domain.Account;
import com.rebwon.demosecurityboard.modules.account.domain.AuthAccount;
import com.rebwon.demosecurityboard.modules.post.api.payload.PostUpdatePayload;
import com.rebwon.demosecurityboard.modules.post.domain.Post;
import com.rebwon.demosecurityboard.modules.post.service.PostService;
import com.rebwon.demosecurityboard.modules.post.api.payload.PostCreatePayload;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
	private final PostService postService;

	@PostMapping
	public ResponseEntity<EntityModel<Post>> create(@RequestBody @Valid PostCreatePayload payload, @AuthAccount Account account) {
		Post newPost = postService.create(payload, account);
		EntityModel<Post> model = EntityModel.of(newPost);
		model.add(linkTo(PostController.class).slash(newPost.getId()).withSelfRel(),
			linkTo(PostController.class).slash(newPost.getId()).withRel("update-post"));
		return ResponseEntity.created(URI.create("/api/posts/" + newPost.getId())).body(model);
	}

	@PutMapping("/{id}")
	public ResponseEntity<EntityModel<Post>> update(@PathVariable Long id, @AuthAccount Account account,
		@RequestBody @Valid PostUpdatePayload payload) {
		Post dbPost = postService.update(id, account, payload);
		EntityModel<Post> model = EntityModel.of(dbPost);
		model.add(linkTo(PostController.class).slash(dbPost.getId()).withSelfRel(),
			linkTo(PostController.class).slash(dbPost.getId()).withRel("update-post"));
		return new ResponseEntity<>(model, HttpStatus.NO_CONTENT);
	}

	@GetMapping("/{id}")
	public ResponseEntity<EntityModel<Post>> findOne(@PathVariable Long id, @AuthAccount Account account) {
		Post dbPost = postService.findOne(id);
		EntityModel<Post> model = EntityModel.of(dbPost);
		if(dbPost.isSameWriter(account)) {
			model.add(linkTo(PostController.class).slash(dbPost.getId()).withRel("update-post"));
		}
		model.add(linkTo(PostController.class).slash(dbPost.getId()).withSelfRel());
		return ResponseEntity.ok(model);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity delete(@PathVariable Long id, @AuthAccount Account account) {
		postService.delete(id, account);
		return ResponseEntity.noContent().build();
	}
}
