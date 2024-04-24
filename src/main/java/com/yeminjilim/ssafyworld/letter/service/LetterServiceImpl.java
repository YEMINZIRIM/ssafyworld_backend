package com.yeminjilim.ssafyworld.letter.service;

import com.yeminjilim.ssafyworld.letter.dto.LetterDTO;
import com.yeminjilim.ssafyworld.letter.dto.LetterDTO.CreateRequest;
import com.yeminjilim.ssafyworld.letter.dto.LetterDTO.CreateResponse;
import com.yeminjilim.ssafyworld.letter.repository.LetterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class LetterServiceImpl implements LetterService {
    private final LetterRepository letterRepository;

    @Override
    public Mono<CreateResponse> createLetter(Long userId, Mono<CreateRequest> request) {
        return request
                .filter((r) -> validate(userId, r))
                .map((r) -> LetterDTO.CreateRequest.toEntity(userId, r))
                .flatMap(letterRepository::save)
                .map(CreateResponse::of);
    }

    private boolean validate(Long userId, CreateRequest request) {
        if (isEqualUser(userId, request.getToUser()))
            return false;

        return true;
    }

    private boolean isEqualUser(Long toUserId, Long fromUserId) {
        if (toUserId.equals(fromUserId))
            return true;

        return false;
    }
}
