package com.yeminjilim.ssafyworld.member.controller;

import com.yeminjilim.ssafyworld.member.dto.QuestionDTO;
import com.yeminjilim.ssafyworld.member.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping
    public Mono<ResponseEntity<List<QuestionDTO>>> findAll() {
        return questionService.findAll()
                .map(QuestionDTO::of)
                .collectList()
                .map(ResponseEntity::ok);
    }

    @GetMapping("{id}")
    public Mono<ResponseEntity<QuestionDTO>> findById(@PathVariable("id") Long id) {

        return questionService.findById(id)
                .map(QuestionDTO::of)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    public Mono<ResponseEntity<QuestionDTO>> save(@RequestBody QuestionDTO request) {

        if(request.getQuestion() == null) {
            throw new RuntimeException("question field is required");
        }

        return questionService.save(request)
                .map(QuestionDTO::of)
                .map(ResponseEntity::ok);
    }

    @PutMapping
    public Mono<ResponseEntity<QuestionDTO>> update(@RequestBody QuestionDTO request) {

        if(request.getQuestion() == null || request.getId() == null) {
            throw new RuntimeException("require field is null");
        }

        return questionService.update(request)
                .map(QuestionDTO::of)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping
    public Mono<ResponseEntity<Void>> delete(@RequestBody QuestionDTO request) {
        Long id = request.getId();
        if(id == null) {
            throw new RuntimeException("required field is null");
        }

        return questionService.delete(id)
                .map(ResponseEntity::ok);
    }




}
