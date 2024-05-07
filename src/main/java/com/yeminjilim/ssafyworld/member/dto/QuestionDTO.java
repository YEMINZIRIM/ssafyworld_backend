package com.yeminjilim.ssafyworld.member.dto;

import com.yeminjilim.ssafyworld.member.entity.Question;
import lombok.*;

@Builder(access = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {

    private Long id;
    private String question;

    public static QuestionDTO of(Question question) {
        return builder()
                .id(question.getId())
                .question(question.getQuestion())
                .build();
    }

}
