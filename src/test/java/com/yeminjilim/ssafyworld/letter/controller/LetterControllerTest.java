package com.yeminjilim.ssafyworld.letter.controller;

import com.yeminjilim.ssafyworld.letter.dto.LetterDTO.CreateRequest;
import com.yeminjilim.ssafyworld.letter.dto.LetterDTO.CreateResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@AutoConfigureWebTestClient
public class LetterControllerTest {
    @Autowired
    private WebClient webClient;

    @Test
    public void createLetterTest() {
        String title = "title입니다";
        String content = "content입니다";
        long receiverId = 2L;
        Mono<ResponseEntity<CreateResponse>> responseMono = this.webClient
                .post()
                .uri("/letter")
                .bodyValue(new CreateRequest(receiverId, title, content))
                .retrieve()
                .toEntity(CreateResponse.class)
                .doOnNext(System.out::println);

        StepVerifier.create(responseMono)
                .assertNext(prod -> {
                    Assertions.assertThat(prod).isNotNull();
                    Assertions.assertThat(prod.getBody().getTitle()).isEqualTo(title);
                    Assertions.assertThat(prod.getBody().getContent()).isEqualTo(content);
                    Assertions.assertThat(prod.getBody().getToUser()).isEqualTo(receiverId);
                })
                .verifyComplete();
    }
}
