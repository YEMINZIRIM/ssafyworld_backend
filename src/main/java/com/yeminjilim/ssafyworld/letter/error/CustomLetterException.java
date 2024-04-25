package com.yeminjilim.ssafyworld.letter.error;

import com.yeminjilim.ssafyworld.global.CustomException;

public class CustomLetterException extends CustomException {

	public CustomLetterException(LetterErrorCode errorCode) {
		super(errorCode.getStatus(), errorCode.getCode(), errorCode.getMessage());
	}
}