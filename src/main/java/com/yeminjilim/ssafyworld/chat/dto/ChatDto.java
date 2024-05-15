package com.yeminjilim.ssafyworld.chat.dto;


import com.yeminjilim.ssafyworld.chat.entity.Chat;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

@Builder
@Data
public class ChatDto implements Comparable<ChatDto>{

    private Long chatId;
    private Long senderId;
    private String senderName;
    private String content;
    private Long groupInfoId;
    private String createdAt;

    public static ChatDto of(Chat chat) {
        return builder()
                .chatId(chat.getChatId())
                .senderId(chat.getSenderId())
                .senderName(chat.getSenderName())
                .content(chat.getContent())
                .createdAt(chat.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }

    public Chat toEntity() {
        return Chat.builder()
                .chatId(chatId)
                .senderId(senderId)
                .senderName(senderName)
                .content(content)
                .groupInfoId(groupInfoId)
                .createdAt(LocalDateTime.parse(createdAt, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }

    @Override
    public int compareTo(ChatDto o) {
        return createdAt.compareTo(o.createdAt);
    }
}