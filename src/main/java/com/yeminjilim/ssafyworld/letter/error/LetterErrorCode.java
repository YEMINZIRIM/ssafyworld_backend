package com.yeminjilim.ssafyworld.letter.error;

import org.springframework.http.HttpStatus;

public enum LetterErrorCode {
    CANT_SEND_TO_ME(HttpStatus.BAD_REQUEST,"LETTER-001","자기자신에게 Letter를 보낼 수 없습니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "AUTH-0003", "권한이 없는 사용자입니다."), //TODO AUTH꺼로 변경
    NOT_FOUND(HttpStatus.NOT_FOUND, "LETTER-002", "Letter를 찾을 수 없습니다."); //TODO AUTH꺼로 변경? 회의해보기

    LetterErrorCode(HttpStatus status, String code, String message) {
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
