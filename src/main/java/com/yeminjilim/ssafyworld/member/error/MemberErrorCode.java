package com.yeminjilim.ssafyworld.member.error;

import org.springframework.http.HttpStatus;

public enum MemberErrorCode {

    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND,"MEMBER-001","MEMBER를 찾을 수 없습니다."),
    WRONG_ANSWER(HttpStatus.BAD_REQUEST, "MEMBER-002", "질문에 대한 답이 잘못되었습니다."),
    MEMBER_NOT_DELETE(HttpStatus.BAD_REQUEST, "MEMBER-004", "회원을 삭제할 수 없습니다."),
    INVALID_QUESTION_ANSWER(HttpStatus.BAD_REQUEST, "MEMBER-005", "질문과 답변이 맞지 않습니다.");

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
