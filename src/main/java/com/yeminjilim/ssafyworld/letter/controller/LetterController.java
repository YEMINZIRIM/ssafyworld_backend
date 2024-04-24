package com.yeminjilim.ssafyworld.letter.controller;

import com.yeminjilim.ssafyworld.letter.dto.LetterDTO;
import com.yeminjilim.ssafyworld.letter.service.LetterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/letter")
public class LetterController {
    private final LetterService letterService;

    @PostMapping
    public Mono<ResponseEntity<LetterDTO.CreateResponse>> createLetter(@RequestBody Mono<LetterDTO.CreateRequest> request) {
        Long tmpFromUserId = 1L; //@TODO 로그인 구현 시 삭제

        return letterService.createLetter(tmpFromUserId, request)
                .map(ResponseEntity::ok)
                .onErrorResume(Exception.class,
                        ex -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .build())); //@TODO customError로 변경
    }
}
