package com.rebwon.demosecurityboard.modules.account.api.exception;

import com.rebwon.demosecurityboard.modules.common.error.ErrorCode;
import com.rebwon.demosecurityboard.modules.common.exception.BusinessException;

public class NicknameDuplicateException extends BusinessException {
	public NicknameDuplicateException(String nickname) {
		super(nickname, ErrorCode.NICKNAME_DUPLICATION);
	}
}
