package com.yeminjilim.ssafyworld.config;

import com.yeminjilim.ssafyworld.global.CustomException;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
public class ErrorResponse {

    private String code;
    private String message;

    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ResponseEntity<ErrorResponse> of(CustomException exception) {

        ErrorResponse body = new ErrorResponse(exception.getErrorCode(), exception.getMessage());
        return ResponseEntity
                .status(exception.getHttpStatus())
                .body(body);
    }

}