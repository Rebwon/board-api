package com.rebwon.demosecurityboard.modules.account.api.payload;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpPayload {
	@Email
	@NotBlank
	private String email;
	@NotBlank
	@Length(min = 8, max = 50)
	private String password;
	@NotBlank
	private String nickname;
}
