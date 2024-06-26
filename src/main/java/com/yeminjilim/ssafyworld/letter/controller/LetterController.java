package com.yeminjilim.ssafyworld.letter.controller;

import com.yeminjilim.ssafyworld.jwt.JWTProvider;
import com.yeminjilim.ssafyworld.letter.dto.LetterDTO;
import com.yeminjilim.ssafyworld.letter.dto.LetterDTO.ReceivedLetterResponse;
import com.yeminjilim.ssafyworld.letter.dto.LetterDTO.SentLetterResponse;
import com.yeminjilim.ssafyworld.letter.service.LetterService;
import com.yeminjilim.ssafyworld.member.entity.Member;
import com.yeminjilim.ssafyworld.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;

import static com.yeminjilim.ssafyworld.letter.dto.LetterDTO.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/letter")
public class LetterController {
    private final LetterService letterService;
    private final MemberService memberService;
    private final JWTProvider jwtProvider;

    @PostMapping
    public Mono<ResponseEntity<CreateResponse>> createLetter(@RequestBody Mono<CreateRequest> request, ServerHttpRequest serverHttpRequest) {
        return getUser(serverHttpRequest)
                .flatMap((r) ->
                    letterService.createLetter(r.getMemberInfo().getMemberId(), request)
                ).map(ResponseEntity::ok);
    }

    @GetMapping("/{letterId}")
    public Mono<ResponseEntity<ReceivedLetterResponse>> getLetterDetail(@PathVariable Long letterId, ServerHttpRequest serverHttpRequest) {
        return getUser(serverHttpRequest)
                .flatMap((member) ->
                        letterService.findByLetterId(member.getMemberInfo().getMemberId(), letterId)
                )
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/receive")
    public Mono<ResponseEntity<Mono<List<ReceivedLetterResponse>>>> getAllReceivedLetters(ServerHttpRequest serverHttpRequest) {
        Mono<List<ReceivedLetterResponse>> letters = getUser(serverHttpRequest)
                .flux()
                .flatMap((member) -> letterService.findAllReceivedLetters(member.getMemberInfo().getMemberId()))
                .collectList()
                .doOnNext(list -> list.sort(Comparator.comparing(ReceivedLetterResponse::getCreatedAt)));

        return Mono.just(ResponseEntity.ok().body(letters))
                .onErrorResume(Exception.class,
                        ex -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .build())); // TODO : customError로 변경
    }

    @GetMapping("/send")
    public Mono<ResponseEntity<Mono<List<SentLetterResponse>>>> getAllSentLetters(ServerHttpRequest serverHttpRequest) {
        Mono<List<SentLetterResponse>> sentLetterResponseFlux = getUser(serverHttpRequest)
                .flux()
                .flatMap(r -> letterService.findAllSentLetters(r.getMemberInfo().getMemberId()))
                .collectList()
                .doOnNext(list -> list.sort(Comparator.comparing(SentLetterResponse::getCreatedAt)));

        return Mono.just(ResponseEntity.ok().body(sentLetterResponseFlux))
                .onErrorResume(Exception.class,
                        ex -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .build())); // TODO : customError로 변경
    }

    @DeleteMapping("/{letterId}") //보낸 사람만 삭제가능
    public Mono<Void> deleteLetter(@PathVariable Long letterId, ServerHttpRequest serverHttpRequest) {
        return getUser(serverHttpRequest)
                .flatMap((member) -> letterService.deleteLetter(letterId, member.getMemberInfo().getMemberId()));
    }

    //나에게 온 편지 숨기기
    @PostMapping("/hidden")
    public Mono<ResponseEntity> hideLetter(@RequestBody Mono<HideRequest> request, ServerHttpRequest serverHttpRequest) {
        return getUser(serverHttpRequest)
                .flatMap((member) -> letterService.hideLetter(request, member))
                .map((letter) -> HideRequest.builder().letterId(letter.getId()).hidden(letter.getHidden()).build())
                .map(ResponseEntity::ok);
    }

    @GetMapping("/hidden")
    public Mono<ResponseEntity<Mono<List<ReceivedLetterResponse>>>> getHideLetter(ServerHttpRequest serverHttpRequest) {
        return getUser(serverHttpRequest)
                .map((member) -> {
                    Flux<ReceivedLetterResponse> hideLetter = letterService.getHideLetter(member.getMemberInfo().getMemberId());
                    return hideLetter.collectList()
                            .doOnNext(list -> list.sort(Comparator.comparing(ReceivedLetterResponse::getCreatedAt)));
                })
                .map(ResponseEntity::ok);
    }

    private Mono<Member> getUser(ServerHttpRequest serverHttpRequest) {
        return memberService.findBySub(getUserSub(serverHttpRequest));
    }

    private String getUserSub(ServerHttpRequest serverHttpRequest) {
        Authentication authentication = jwtProvider.getToken(serverHttpRequest);

        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
        String sub = userDetails.getUsername();

        return sub;
    }
}
