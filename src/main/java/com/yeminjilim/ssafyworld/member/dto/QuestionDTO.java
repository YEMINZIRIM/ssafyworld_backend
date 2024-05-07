package com.yeminjilim.ssafyworld.member.dto;

import com.yeminjilim.ssafyworld.member.entity.Question;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

@Builder(access = AccessLevel.PRIVATE)
@Data
public class QuestionDTO {

    private Long id;
    private String question;

    public static QuestionDTO of(Question question) {
        return builder()
                .id(question.getId())
                .question(question.getQuestion())
                .build();
    }


    @Data
    public static class QuestionMatchingDTO {
        private Long id;
        private String answer;
    }

}
