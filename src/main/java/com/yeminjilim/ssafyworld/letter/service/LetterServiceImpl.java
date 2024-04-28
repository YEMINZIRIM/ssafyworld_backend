package com.yeminjilim.ssafyworld.letter.service;

import com.yeminjilim.ssafyworld.letter.dto.LetterDTO;
import com.yeminjilim.ssafyworld.letter.dto.LetterDTO.ReceivedLetterResponse;
import com.yeminjilim.ssafyworld.letter.dto.LetterDTO.CreateRequest;
import com.yeminjilim.ssafyworld.letter.dto.LetterDTO.CreateResponse;
import com.yeminjilim.ssafyworld.letter.repository.LetterRepository;
import com.yeminjilim.ssafyworld.member.repository.GroupInfoRepository;
import com.yeminjilim.ssafyworld.member.repository.MemberInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class LetterServiceImpl implements LetterService {
    private final LetterRepository letterRepository;
    private final MemberInfoRepository memberInfoRepository;
    private final GroupInfoRepository groupInfoRepository;

    @Override
    public Mono<CreateResponse> createLetter(Long userId, Mono<CreateRequest> request) throws RuntimeException {
        return request
                .filter((req) -> validate(userId, req))
                .map((r) -> LetterDTO.CreateRequest.toEntity(userId, r))
                .flatMap(letterRepository::save)
                .map(CreateResponse::of);
    }

    @Override
    public Flux<LetterDTO.ReceivedLetterResponse> findAllReceivedLetters(Long userId) {
        return letterRepository.findAllByToUser(userId)
                .map(ReceivedLetterResponse::of);
    }

    @Override
    public Mono<LetterDTO.ReceivedLetterResponse> findByLetterId(Long letterId) {
        return letterRepository.findById(letterId)
                .map(ReceivedLetterResponse::of);
    }

    @Override
    public Flux<LetterDTO.SentLetterResponse> findAllSentLetters(Long userId) {
        return letterRepository.findAllByFromUser(userId)
                .flatMap(letter ->
                        memberInfoRepository.findById(userId)
                                .flatMap(memberInfo ->
                                        groupInfoRepository.findById(memberInfo.getGroupInfoId())
                                                .map(groupInfo ->
                                                        LetterDTO.SentLetterResponse.of(letter, memberInfo, groupInfo))
                                )
                );
    }

    private boolean validate(Long userId, CreateRequest request) {
        if (isEqualUser(userId, request.getToUser()))
            throw new RuntimeException();

        return true;
    }

    private boolean isEqualUser(Long toUserId, Long fromUserId) {
        if (toUserId.equals(fromUserId))
            return true;

        return false;
    }
}
