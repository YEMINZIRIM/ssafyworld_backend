package com.yeminjilim.ssafyworld.member.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@Data
@Table("question")
public class Question {

    @Id
    @Column("id")
    private Long id;

    @Column("question")
    private String question;

    public Question(Long id, String question) {
        this.id = id;
        this.question = question;
    }
}
