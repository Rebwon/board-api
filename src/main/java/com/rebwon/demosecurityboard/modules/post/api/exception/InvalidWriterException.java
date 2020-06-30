package com.rebwon.demosecurityboard.modules.post.api.exception;

import com.rebwon.demosecurityboard.modules.common.error.ErrorCode;
import com.rebwon.demosecurityboard.modules.common.exception.BusinessException;

public class InvalidWriterException extends BusinessException {
	public InvalidWriterException(String message) {
		super(message, ErrorCode.INVALID_WRITER);
	}
}
