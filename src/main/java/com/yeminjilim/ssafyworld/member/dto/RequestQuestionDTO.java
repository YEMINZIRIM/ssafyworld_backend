package com.yeminjilim.ssafyworld.member.dto;

import lombok.*;

@Builder(access = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestQuestionDTO {

    private Long questionId;
    private String answer;

}
