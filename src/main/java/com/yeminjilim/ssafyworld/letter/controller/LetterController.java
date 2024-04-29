package com.yeminjilim.ssafyworld.letter.controller;

import com.yeminjilim.ssafyworld.letter.dto.LetterDTO;
import com.yeminjilim.ssafyworld.letter.service.LetterService;
import com.yeminjilim.ssafyworld.member.entity.Member;
import com.yeminjilim.ssafyworld.member.entity.MemberInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
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
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{letterId}")
    public Mono<ResponseEntity<LetterDTO.ReceivedLetterResponse>> getLetterDetail(@PathVariable Long letterId) {
        return letterService.findByLetterId(letterId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorResume(Exception.class,
                        ex -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .build())); // TODO : custom Error 로 변경
    }

    @GetMapping("/receive/{userId}")  // TODO : 사용자 정보(UserId) Header 에서 받아오기
    public Mono<ResponseEntity<Flux<LetterDTO.ReceivedLetterResponse>>> getAllReceivedLetters(@PathVariable Long userId) {
        Flux<LetterDTO.ReceivedLetterResponse> letters = letterService.findAllReceivedLetters(userId);
        return Mono.just(ResponseEntity.ok().body(letters))
                .onErrorResume(Exception.class,
                        ex -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .build())); // TODO : customError로 변경
    }

    @GetMapping("/send/{userId}")  // TODO : 사용자 정보(UserId) Header 에서 받아오기
    public Mono<ResponseEntity<Flux<LetterDTO.SentLetterResponse>>> getAllSentLetters(@PathVariable Long userId) {
        Flux<LetterDTO.SentLetterResponse> letters = letterService.findAllSentLetters(userId);
        return Mono.just(ResponseEntity.ok().body(letters))
                .onErrorResume(Exception.class,
                        ex -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .build())); // TODO : customError로 변경
    }

    @DeleteMapping("/letter/{letterId}") //보낸 사람만 삭제가능
    public Mono<Void> deleteLetter(@PathVariable Long letterId) {
        //TODO 로그인 구현 시 삭제
        Long tmpFromUserId = 1L;
        MemberInfo tmpMemberInfo = MemberInfo.builder().memberId(tmpFromUserId).build();
        Member member = new Member(tmpMemberInfo, null);

        return letterService.deleteLetter(letterId, member);
    }
}
