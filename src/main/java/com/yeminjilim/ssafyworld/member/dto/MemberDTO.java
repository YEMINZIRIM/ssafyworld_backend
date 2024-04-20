package com.yeminjilim.ssafyworld.member.dto;


import com.yeminjilim.ssafyworld.member.entity.MemberInfo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

@Builder(access = AccessLevel.PRIVATE)
@Data
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
                .serialNumber(serialNumber)
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
                .questionId(member.getQuestionId())
                .answer(member.getAnswer())
                .build();
    }

}
