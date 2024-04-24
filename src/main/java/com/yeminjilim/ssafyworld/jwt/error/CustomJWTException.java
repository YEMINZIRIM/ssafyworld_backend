package com.yeminjilim.ssafyworld.jwt.error;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class CustomJWTException extends AuthenticationException {

    private JWTErrorCode errorCode;
    public CustomJWTException(JWTErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
