package com.yeminjilim.ssafyworld.jwt.error;

import lombok.Getter;
import org.springframework.security.access.AccessDeniedException;


@Getter
public class CustomAccessDeniedException extends AccessDeniedException {
    private JWTErrorCode errorCode;

    public CustomAccessDeniedException(JWTErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
