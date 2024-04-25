package com.yeminjilim.ssafyworld.letter.controller;

import com.yeminjilim.ssafyworld.config.ErrorResponse;
import com.yeminjilim.ssafyworld.letter.error.CustomLetterException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class LetterControllerAdvice {
    @ExceptionHandler(CustomLetterException.class)
    public ResponseEntity letterExceptionHandler(CustomLetterException e) {

        return ResponseEntity.status(e.getHttpStatus()).body(ErrorResponse.of(e));
    }
}
