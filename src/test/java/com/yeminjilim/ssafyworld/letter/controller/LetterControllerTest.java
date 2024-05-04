package com.yeminjilim.ssafyworld.letter.controller;

import com.yeminjilim.ssafyworld.letter.dto.LetterDTO.CreateRequest;
import com.yeminjilim.ssafyworld.letter.dto.LetterDTO.CreateResponse;
import com.yeminjilim.ssafyworld.letter.dto.LetterDTO.ReceivedLetterResponse;
import com.yeminjilim.ssafyworld.letter.error.CustomLetterException;
import com.yeminjilim.ssafyworld.letter.error.LetterErrorCode;
import com.yeminjilim.ssafyworld.util.CustomStringUtil;
import java.util.concurrent.atomic.AtomicReference;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@AutoConfigureWebTestClient
public class LetterControllerTest {
    public static final int TITLE_MAX_SIZE = 50;
    public static final int TITLE_GENERAL_SIZE = 15;
    public static final int CONTENT_MAX_SIZE = 5000;
    public static final int CONTENT_GENERAL_SIZE = 100;
    public static String TITLE = "";
    public static String CONTENT = "";
    public static long RECEIVER_ID = 2L;

    @Autowired
    private WebClient webClient;

    @Test
    @Order(1)
    public void createLetterTest() {
        TITLE = CustomStringUtil.makeRandomString(TITLE_GENERAL_SIZE);
        CONTENT = CustomStringUtil.makeRandomString(CONTENT_GENERAL_SIZE);

        Mono<ResponseEntity<CreateResponse>> responseMono = this.webClient
                .post()
                .uri("/letter")
                .bodyValue(new CreateRequest(RECEIVER_ID, TITLE, CONTENT))
                .retrieve()
                .toEntity(CreateResponse.class)
                .doOnNext(System.out::println);

        StepVerifier.create(responseMono)
                .assertNext(prod -> {
                    Assertions.assertThat(prod).isNotNull();
                    Assertions.assertThat(prod.getBody().getTitle()).isEqualTo(TITLE);
                    Assertions.assertThat(prod.getBody().getContent()).isEqualTo(CONTENT);
                    Assertions.assertThat(prod.getBody().getToUser()).isEqualTo(RECEIVER_ID);
                })
                .verifyComplete();
    }

    @Test
    @Order(1)
    public void createLetter_maxSize() {
        TITLE = CustomStringUtil.makeRandomString(TITLE_MAX_SIZE);
        CONTENT = CustomStringUtil.makeRandomString(CONTENT_MAX_SIZE);

        Mono<ResponseEntity<CreateResponse>> responseMono = this.webClient
                .post()
                .uri("/letter")
                .bodyValue(new CreateRequest(RECEIVER_ID, TITLE, CONTENT))
                .retrieve()
                .toEntity(CreateResponse.class)
                .doOnNext(System.out::println);

        StepVerifier.create(responseMono)
                .assertNext(prod -> {
                    Assertions.assertThat(prod).isNotNull();
                    Assertions.assertThat(prod.getBody().getTitle()).isEqualTo(TITLE);
                    Assertions.assertThat(prod.getBody().getContent()).isEqualTo(CONTENT);
                    Assertions.assertThat(prod.getBody().getToUser()).isEqualTo(RECEIVER_ID);
                })
                .verifyComplete();
    }

    @Test
    @Order(1)
    public void createLetter_overSize() {
        TITLE = CustomStringUtil.makeRandomString(30);
        CONTENT = CustomStringUtil.makeRandomString(5001);

        Mono<ResponseEntity<ReceivedLetterResponse>> receivedResponse = this.webClient
                .post()
                .uri("/letter")
                .bodyValue(new CreateRequest(RECEIVER_ID, TITLE, CONTENT))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response ->
                        Mono.error(
                                new CustomLetterException(
                                        LetterErrorCode.NOT_FOUND
                                )
                        )
                ).toEntity(ReceivedLetterResponse.class);

        StepVerifier.create(receivedResponse)
                .expectErrorMatches(response ->
                        response instanceof CustomLetterException)
                .verify();
    }

    @Test
    @Order(2)
    public void getLetterDetail() {
        long receiverId = 2L;
        AtomicReference<Long> savedId = new AtomicReference<>(0L);

        Mono<ResponseEntity<CreateResponse>> responseMono = this.webClient
                .post()
                .uri("/letter")
                .bodyValue(new CreateRequest(receiverId, TITLE, CONTENT))
                .retrieve()
                .toEntity(CreateResponse.class)
                .doOnNext((r) -> {
                    savedId.set(r.getBody().getLetterId());
                });

        StepVerifier.create(responseMono)
                .assertNext(prod -> {
                    Assertions.assertThat(prod).isNotNull();
                })
                .verifyComplete();

        Mono<ResponseEntity<ReceivedLetterResponse>> responseEntityMono = this.webClient
                .get()
                .uri("/letter/{letterId}", savedId.get())
                .retrieve()
                .toEntity(ReceivedLetterResponse.class)
                .doOnNext(System.out::println);

        StepVerifier.create(responseEntityMono)
                .assertNext(prod -> {
                    Assertions.assertThat(prod).isNotNull();
                    Assertions.assertThat(prod.getBody().getTitle()).isEqualTo(TITLE);
                    Assertions.assertThat(prod.getBody().getContent()).isEqualTo(CONTENT);
                })
                .verifyComplete();
    }

    @Test
    @Order(2)
    public void getLetterDetail_NOT_FOUND() {
        Mono<ResponseEntity<ReceivedLetterResponse>> responseEntityMono = this.webClient
                .get()
                .uri("/letter/{letterId}", -1L)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response ->
                        Mono.error(
                                new CustomLetterException(
                                        LetterErrorCode.NOT_FOUND
                                )
                        )
                ).toEntity(ReceivedLetterResponse.class);

        StepVerifier.create(responseEntityMono)
                .expectErrorMatches(response ->
                        response instanceof CustomLetterException)
                .verify();
    }
}
