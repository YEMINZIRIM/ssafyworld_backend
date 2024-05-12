package com.yeminjilim.ssafyworld.member.dto;


import com.yeminjilim.ssafyworld.member.entity.MemberInfo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {

    private Long memberId;
    private String sub;
    private String provider;
    private Long groupInfoId;
    private String name;
    private String serialNumber;
    private Long questionId;
    private String answer;

    public MemberInfo toEntity() {
        return MemberInfo.builder()
                .memberId(memberId)
                .sub(sub)
                .provider(provider)
                .groupInfoId(groupInfoId)
                .name(name)
                .serialNumber(UUID.randomUUID().toString().substring(0,8))
                .questionId(questionId)
                .answer(answer)
                .build();
    }

    public static MemberDTO toDTO(MemberInfo member) {
        return MemberDTO.builder()
                .memberId(member.getMemberId())
                .sub(member.getSub())
                .provider(member.getProvider())
                .groupInfoId(member.getGroupInfoId())
                .name(member.getName())
                .serialNumber(member.getSerialNumber())
                .build();
    }

}
