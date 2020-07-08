package com.rebwon.demosecurityboard.modules.activity.exception;

import com.rebwon.demosecurityboard.modules.common.error.ErrorCode;
import com.rebwon.demosecurityboard.modules.common.exception.BusinessException;

public class ActivityNotFoundException extends BusinessException {
	public ActivityNotFoundException() {
		super(ErrorCode.NOT_FOUND);
	}
}
