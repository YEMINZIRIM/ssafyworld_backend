package com.yeminjilim.ssafyworld.global;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {

    private HttpStatus status;
    private String errorCode;

    public CustomException(HttpStatus status, String errorCode,String message) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return status;
    }
}