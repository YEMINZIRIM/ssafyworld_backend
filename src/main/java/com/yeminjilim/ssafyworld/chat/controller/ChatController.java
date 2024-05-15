package com.yeminjilim.ssafyworld.chat.controller;

import com.yeminjilim.ssafyworld.chat.dto.ChatDto;
import com.yeminjilim.ssafyworld.chat.entity.Chat;
import com.yeminjilim.ssafyworld.chat.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@RestController
public class ChatController {

    @Autowired
    private ChatRepository chatRepository;

    @GetMapping("/chat/{groupInfoId}/messages")
    public Mono<List<ChatDto>> getChatMessages(@PathVariable String groupInfoId) {
        return chatRepository.findByGroupInfoIdOrderByCreatedAtDesc(groupInfoId)
                .map(ChatDto::of)
                .collectList()
                .doOnNext(list -> list.sort(Collections.reverseOrder()));

    }
}
