package com.yeminjilim.ssafyworld.member.service;

import com.yeminjilim.ssafyworld.member.dto.QuestionDTO;
import com.yeminjilim.ssafyworld.member.entity.Question;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface QuestionService {

    Flux<Question> findAll();

    Mono<Question> findById(Long id);

    Mono<Question> save(QuestionDTO questionDTO);

    Mono<Question> update(QuestionDTO questionDTO);

    Mono<Void> delete(Long id);
}
