package com.yeminjilim.ssafyworld.letter.service;

import com.yeminjilim.ssafyworld.letter.dto.LetterDTO.CreateRequest;
import com.yeminjilim.ssafyworld.letter.dto.LetterDTO.CreateResponse;
import reactor.core.publisher.Mono;

public interface LetterService {
    Mono<CreateResponse> createLetter(Long userId, Mono<CreateRequest> request);
}
