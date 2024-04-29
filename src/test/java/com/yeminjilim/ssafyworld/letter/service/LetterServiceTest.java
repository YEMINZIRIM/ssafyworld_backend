package com.yeminjilim.ssafyworld.letter.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.yeminjilim.ssafyworld.letter.dto.LetterDTO;
import com.yeminjilim.ssafyworld.letter.dto.LetterDTO.CreateResponse;
import com.yeminjilim.ssafyworld.letter.entity.Letter;
import com.yeminjilim.ssafyworld.letter.error.CustomLetterException;
import com.yeminjilim.ssafyworld.letter.repository.LetterRepository;
import com.yeminjilim.ssafyworld.member.entity.Member;
import com.yeminjilim.ssafyworld.member.entity.MemberInfo;
import com.yeminjilim.ssafyworld.member.repository.GroupInfoRepository;
import com.yeminjilim.ssafyworld.member.repository.MemberInfoRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class LetterServiceTest {
    @Mock
    LetterRepository letterRepository;
    @Mock
    MemberInfoRepository memberInfoRepository;
    @Mock
    GroupInfoRepository groupInfoRepository;

    LetterService letterService;

    //setting값
    long toUserId = 1L;
    long fromUserId = 2L;
    String testTitle = "this is title";
    String testContent = "this is content";
    Letter mockLetter;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        letterService = new LetterServiceImpl(letterRepository, memberInfoRepository, groupInfoRepository);

        //letterRepository에서 반환할 db에 저장된 Letter
        mockLetter = new Letter(1L, toUserId, fromUserId, testTitle, testContent, false, LocalDateTime.now(), LocalDateTime.now());
        Mono<Letter> mockLetterMono = Mono.just(mockLetter);
        given(letterRepository.save(any(Letter.class))).willReturn(mockLetterMono);
    }

    @Test
    @Order(1)
    public void create_letter_successful_returns_letter(){
        //letterService에 들어올 dto
        LetterDTO.CreateRequest mockLetterDto = new LetterDTO.CreateRequest(toUserId, testTitle, testContent);
        Mono<LetterDTO.CreateRequest> mockLetterDtoMono = Mono.just(mockLetterDto);

        //service호출
        Mono<CreateResponse> letter = letterService.createLetter(2L, mockLetterDtoMono);

        Assertions.assertNotNull(letter);
        StepVerifier.create(letter)
                .thenConsumeWhile(
                        result -> {
                            Assertions.assertNotNull(result);
                            Assert.isTrue(mockLetterDto.getContent().equals(result.getContent()),"body not equal");
                            Assert.isTrue(mockLetterDto.getTitle().equals(result.getTitle()), "title not equal");
                            return true;
                        })
                .verifyComplete();

        //mock 검증 - letterRepository mock객체가 1번 호출되었는지 확인, save메서드가 Letter 인스턴스를 매개변수로 받는지 확인
        verify(letterRepository, times(1)).save(any(Letter.class));
    }

    @Test
    public void create_letter_cant_send_me(){
        //letterService에 들어올 dto
        LetterDTO.CreateRequest mockLetterDto = new LetterDTO.CreateRequest(fromUserId, testTitle, testContent); //자기 자신에게 보냄
        Mono<LetterDTO.CreateRequest> mockLetterDtoMono = Mono.just(mockLetterDto);

        //service호출
        Mono<CreateResponse> letter = letterService.createLetter(fromUserId, mockLetterDtoMono);

        //customeLetterException이 발생하는지 확인
        StepVerifier.create(letter)
                .verifyError(CustomLetterException.class);
    }

    @Test
    @Order(2)
    public void delete_letter(){
        given(letterRepository.findById(1L)).willReturn(Mono.just(mockLetter));
        given(letterRepository.delete(any())).willReturn(Mono.empty());

        StepVerifier.create(Mono.empty())
                .then(() -> letterService.deleteLetter(1L, new Member(MemberInfo.builder().memberId(fromUserId).build(), null)))
                .verifyComplete();
    }

    @Test
    @Order(2)
    public void delete_forbidden(){
        given(letterRepository.findById(1L)).willReturn(Mono.just(mockLetter));
        given(letterRepository.delete(any())).willReturn(Mono.empty());
        Mono<Void> voidMono = letterService.deleteLetter(1L,
                new Member(MemberInfo.builder().memberId(toUserId).build(), null));

        StepVerifier.create(voidMono)
                .verifyError(CustomLetterException.class);
    }

    @Test
    @Order(2)
    public void delete_notfound(){
        given(letterRepository.findById(1L)).willReturn(Mono.empty());
        given(letterRepository.delete(any())).willReturn(Mono.empty());
        Mono<Void> voidMono = letterService.deleteLetter(1L,
                new Member(MemberInfo.builder().memberId(toUserId).build(), null));

        StepVerifier.create(voidMono)
                .verifyError(CustomLetterException.class);
    }
}
