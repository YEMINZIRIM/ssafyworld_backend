package com.yeminjilim.ssafyworld.member.dto;

import com.yeminjilim.ssafyworld.member.entity.MemberInfo;
import lombok.*;

@Builder(access = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMemberDto {

    private String name;
    private Long questionId;
    private String answer;

    public MemberInfo toEntity() {
        return MemberInfo.builder()
                .name(name)
                .questionId(questionId)
                .answer(answer)
                .build();
    }
}
