package com.yeminjilim.ssafyworld.letter.service;

import com.yeminjilim.ssafyworld.letter.dto.LetterDTO;
import com.yeminjilim.ssafyworld.letter.dto.LetterDTO.ReceivedLetterResponse;
import com.yeminjilim.ssafyworld.letter.dto.LetterDTO.CreateRequest;
import com.yeminjilim.ssafyworld.letter.dto.LetterDTO.CreateResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LetterService {
    Mono<CreateResponse> createLetter(Long userId, Mono<CreateRequest> request);

    Flux<ReceivedLetterResponse> findAllReceivedLetters(Long userId);

    Mono<ReceivedLetterResponse> findByLetterId(Long letterId);

    Flux<LetterDTO.SentLetterResponse> findAllSentLetters(Long userId);

}
