package com.yeminjilim.ssafyworld.chat.controller;

import com.yeminjilim.ssafyworld.chat.entity.Chat;
import com.yeminjilim.ssafyworld.chat.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ChatController {

    @Autowired
    private ChatRepository chatRepository;

    @GetMapping("/chat/{groupInfoId}/messages")
    public Flux<Chat> getChatMessages(@PathVariable String groupInfoId) {
        return chatRepository.findByGroupInfoIdOrderByCreatedAtAsc(groupInfoId, PageRequest.of(0, 200));
    }
}
