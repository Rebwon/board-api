package com.rebwon.demosecurityboard.modules.post.api.exception;

import com.rebwon.demosecurityboard.modules.common.error.ErrorCode;
import com.rebwon.demosecurityboard.modules.common.exception.BusinessException;

public class PostNotFoundException extends BusinessException {
	public PostNotFoundException() {
		super(ErrorCode.NOT_FOUND);
	}
}
