package com.yeminjilim.ssafyworld.letter.service;

import com.yeminjilim.ssafyworld.letter.dto.LetterDTO;
import com.yeminjilim.ssafyworld.letter.dto.LetterDTO.HideRequest;
import com.yeminjilim.ssafyworld.letter.dto.LetterDTO.ReceivedLetterResponse;
import com.yeminjilim.ssafyworld.letter.dto.LetterDTO.CreateRequest;
import com.yeminjilim.ssafyworld.letter.dto.LetterDTO.CreateResponse;
import com.yeminjilim.ssafyworld.letter.entity.Letter;
import com.yeminjilim.ssafyworld.member.entity.Member;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LetterService {
    Mono<CreateResponse> createLetter(Long userId, Mono<CreateRequest> request);

    Flux<ReceivedLetterResponse> findAllReceivedLetters(Long userId);

    Mono<ReceivedLetterResponse> findByLetterId(Long userId, Long letterId);

    Flux<LetterDTO.SentLetterResponse> findAllSentLetters(Long userId);

    Mono<Void> deleteLetter(Long letterId, Long userId);

    Mono<Letter> hideLetter(Mono<HideRequest>  request, Member member);

    Flux<ReceivedLetterResponse> getHideLetter(Long userId);
}
