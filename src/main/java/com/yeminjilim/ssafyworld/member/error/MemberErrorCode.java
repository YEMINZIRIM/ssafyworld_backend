package com.yeminjilim.ssafyworld.member.error;

import org.springframework.http.HttpStatus;

public enum MemberErrorCode {

    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND,"MEMBER-001","MEMBER를 찾을 수 없습니다.")
    ;

    MemberErrorCode(HttpStatus status,String code, String message) {
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
