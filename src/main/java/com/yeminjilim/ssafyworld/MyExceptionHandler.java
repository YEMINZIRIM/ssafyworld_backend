package com.yeminjilim.ssafyworld;

import com.yeminjilim.ssafyworld.config.ErrorResponse;
import com.yeminjilim.ssafyworld.member.error.CustomMemberException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice(annotations = RestController.class)
public class MyExceptionHandler {
    @ExceptionHandler(CustomMemberException.class)
    public Mono<ResponseEntity<ErrorResponse>> handle(CustomMemberException e){
        return Mono.just(ErrorResponse.of(e));
    }
}