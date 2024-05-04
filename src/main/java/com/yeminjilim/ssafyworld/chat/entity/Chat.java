package com.yeminjilim.ssafyworld.chat.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chat")
public class Chat {

    @Id
    @Column("chat_id")
    private Long chatId;

    @Column("sender_id")
    private Long senderId;

    @Column("sender_name")
    private String senderName;

    private String content;

    @Column("group_info")
    private Long groupInfoId;

    @CreatedDate
    private LocalDateTime createdAt;
}
