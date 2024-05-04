package com.yeminjilim.ssafyworld.chat.dto;


import com.yeminjilim.ssafyworld.chat.entity.Chat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class ChatDto {

    private Long chatId;
    private Long senderId;
    private String senderName;
    private String content;
    private Long groupInfoId;

    public Chat toEntity() {
        return Chat.builder()
                .chatId(chatId)
                .senderId(senderId)
                .senderName(senderName)
                .content(content)
                .groupInfoId(groupInfoId)
                .build();
    }
}