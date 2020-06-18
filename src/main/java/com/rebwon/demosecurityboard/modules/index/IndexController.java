package com.rebwon.demosecurityboard.modules.index;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rebwon.demosecurityboard.modules.account.api.AccountController;

@RestController
public class IndexController {

	@GetMapping("/api")
	public RepresentationModel index() {
		var index = new RepresentationModel<>();
		index.add(linkTo(AccountController.class).withRel("accounts"));
		return index;
	}
}
