package com.yeminjilim.ssafyworld.letter.service;

import com.yeminjilim.ssafyworld.letter.dto.LetterDTO;
import com.yeminjilim.ssafyworld.letter.dto.LetterDTO.HideRequest;
import com.yeminjilim.ssafyworld.letter.dto.LetterDTO.ReceivedLetterResponse;
import com.yeminjilim.ssafyworld.letter.dto.LetterDTO.CreateRequest;
import com.yeminjilim.ssafyworld.letter.dto.LetterDTO.CreateResponse;
import com.yeminjilim.ssafyworld.letter.entity.Letter;
import com.yeminjilim.ssafyworld.letter.error.CustomLetterException;
import com.yeminjilim.ssafyworld.letter.error.LetterErrorCode;
import com.yeminjilim.ssafyworld.letter.repository.LetterRepository;
import com.yeminjilim.ssafyworld.member.entity.Member;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class LetterServiceImpl implements LetterService {
    private final LetterRepository letterRepository;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private LocalDateTime canReadTime;

    @Autowired
    public LetterServiceImpl(
            LetterRepository letterRepository,
            R2dbcEntityTemplate r2dbcEntityTemplate,
            @Value("${read.time}") String canReadTimeStr
    ) {
        this.letterRepository = letterRepository;
        this.r2dbcEntityTemplate = r2dbcEntityTemplate;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.canReadTime = LocalDateTime.parse(canReadTimeStr, formatter);
    }

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
        return Flux.just("")
                .filter((tmp) -> canReadLetter()) //시간이 되어야 확인가능
                .switchIfEmpty(Mono.error(new CustomLetterException(LetterErrorCode.NOT_TIME_YET)))
                .flatMap((tmp) -> letterRepository.findAllByToUser(userId))
                .map(ReceivedLetterResponse::of);
    }

    @Override
    public Mono<LetterDTO.ReceivedLetterResponse> findByLetterId(Long userId, Long letterId) {
        return letterRepository.findById(letterId)
                .filter((letter) -> letter.getFromUser() != userId && canReadLetter()) //시간이 되어야 확인가능
                .switchIfEmpty(Mono.error(new CustomLetterException(LetterErrorCode.NOT_TIME_YET)))
                .map(ReceivedLetterResponse::of);
    }

    @Override
    public Flux<LetterDTO.SentLetterResponse> findAllSentLetters(Long userId) {
        String query = "SELECT l.id AS letterId, g.ordinal AS toUserOrdinal, g.region AS toUserRegion, " +
                "g.ban AS toUserBan, m.name AS toUserName, l.title, l.content, l.createdAt " +
                "FROM letter l " +
                "JOIN member m ON l.toUser = m.id " +
                "JOIN group_info g ON m.groupInfoId = g.id " +
                "WHERE l.fromUser = :userId";

        return r2dbcEntityTemplate.getDatabaseClient().sql(query)
                .bind("userId", userId)
                .map((row, metadata) -> new LetterDTO.SentLetterResponse(
                        row.get("letterId", Long.class),
                        row.get("toUserOrdinal", Long.class),
                        row.get("toUserRegion", String.class),
                        row.get("toUserBan", Long.class),
                        row.get("toUserName", String.class),
                        row.get("title", String.class),
                        row.get("content", String.class),
                        row.get("createdAt", LocalDateTime.class)))
                .all();
    }

    @Override
    public Mono<Void> deleteLetter(Long letterId, Member member) {
        return letterRepository.findById(letterId)
                .switchIfEmpty(Mono.error(new CustomLetterException(LetterErrorCode.NOT_FOUND))) //letter를 찾을 수 없음
                .filter((letter) -> letter.getFromUser().equals(member.getMemberInfo().getMemberId()))
                .switchIfEmpty(Mono.error(new CustomLetterException(LetterErrorCode.ACCESS_DENIED))) //권한이 없는 사용자
                .flatMap(letterRepository::delete);
    }

    @Override
    public Mono<Letter> hideLetter(Mono<HideRequest> request, Member member) {
        AtomicBoolean hide = new AtomicBoolean(false);

        return request.flatMap((r) -> {
                    hide.set(r.isHidden());
                    return letterRepository.findById(r.getLetterId());
                })
                .switchIfEmpty(Mono.error(new CustomLetterException(LetterErrorCode.NOT_FOUND)))
                .filter((letter) -> letter.getToUser().equals(member.getMemberInfo().getMemberId()))
                .switchIfEmpty(Mono.error(new CustomLetterException(LetterErrorCode.ACCESS_DENIED)))
                .doOnNext((letter) -> {
                    letter.hide(hide.get());
                })
                .flatMap(letterRepository::save);
    }

    @Override
    public Flux<ReceivedLetterResponse> getHideLetter(Long userId) {
        return Flux.just("")
                .filter((tmp) -> canReadLetter()) //시간이 되어야 확인가능
                .switchIfEmpty(Mono.error(new CustomLetterException(LetterErrorCode.NOT_TIME_YET)))
                .flatMap((tmp) -> letterRepository.findAllByToUserAndHiddenIsTrue(userId)) //시간이 되어야 확인가능
                .map(ReceivedLetterResponse::of);
    }

    private boolean validate(Long userId, CreateRequest request) {
        //TODO: 회의 - spring validation을 쓸건가?
        if (userId == null || request.getContent() == null || request.getTitle() == null || request.getToUser() == null) {
            throw new CustomLetterException(LetterErrorCode.BAD_REQUEST);
        }

        if (request.getTitle().length() > 50) {
            throw new CustomLetterException(LetterErrorCode.TITLE_TOO_LONG);
        }

        if (request.getContent().length() > 5000) {
            throw new CustomLetterException(LetterErrorCode.CONTENT_TOO_LONG);
        }

        if (isEqualUser(userId, request.getToUser()))
            throw new CustomLetterException(LetterErrorCode.CANT_SEND_TO_ME);

        return true;
    }

    private boolean isEqualUser(Long toUserId, Long fromUserId) {
        if (toUserId.equals(fromUserId))
            return true;

        return false;
    }

    //letter 오픈 시간 확인
    private boolean canReadLetter() {
        if (LocalDateTime.now().isAfter(canReadTime))
            return true;

        return false;
    }
}
