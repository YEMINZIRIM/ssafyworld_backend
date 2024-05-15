package com.yeminjilim.ssafyworld.member.service;

import com.yeminjilim.ssafyworld.member.dto.QuestionDTO;
import com.yeminjilim.ssafyworld.member.entity.Question;
import com.yeminjilim.ssafyworld.member.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;


    @Override
    public Flux<Question> findAll() {
        return questionRepository.findAll();
    }

    @Override
    public Mono<Question> findById(Long id) {
        return questionRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("question is not exist")));
    }

    @Override
    public Mono<Question> save(QuestionDTO questionDTO) {
        Question question = Question.builder()
                .question(questionDTO.getQuestion())
                .build();

        return questionRepository.save(question);
    }

    @Override
    public Mono<Question> update(QuestionDTO questionDTO) {

        Long id = questionDTO.getId();

        return questionRepository.findById(id)
                .map(question -> {
                    question.setQuestion(questionDTO.getQuestion());
                    return question;
                })
                .flatMap(questionRepository::save);
    }

    @Override
    public Mono<Void> delete(Long id) {
        return questionRepository
                .findById(id)
                .switchIfEmpty(Mono.empty())
                .flatMap(questionRepository::delete);
    }
}







