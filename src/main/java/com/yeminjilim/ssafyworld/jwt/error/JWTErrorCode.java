package com.yeminjilim.ssafyworld.jwt.error;

import org.springframework.http.HttpStatus;

public enum JWTErrorCode {

    EXPIRED_JWT(HttpStatus.UNAUTHORIZED,"AUTH-001","만료된 JWT입니다."),
    INVALID_SIGNATURE(HttpStatus.UNAUTHORIZED,"AUTH-002","JWT의 시그니처가 일치하지 않습니다."),
    AccessDenied(HttpStatus.FORBIDDEN,"AUTH-003","접근 권한이 존재하지 않습니다."),
    Authentication(HttpStatus.UNAUTHORIZED,"AUTH-004","로그인이 필요합니다."),
    INVALID_JWT(HttpStatus.UNAUTHORIZED,"AUTH-005","유효하지 않은 JWT입니다."),
    ;

    JWTErrorCode(HttpStatus status,String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    private final HttpStatus status;
    private final String code;
    private final String message;

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
