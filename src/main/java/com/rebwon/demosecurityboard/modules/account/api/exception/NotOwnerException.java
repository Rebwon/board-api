package com.rebwon.demosecurityboard.modules.account.api.exception;

import com.rebwon.demosecurityboard.modules.common.error.ErrorCode;
import com.rebwon.demosecurityboard.modules.common.exception.BusinessException;

public class NotOwnerException extends BusinessException {
	public NotOwnerException(String nickname) {
		super(nickname, ErrorCode.IS_NOT_OWNER);
	}
}
