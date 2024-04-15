package com.yeminjilim.ssafyworld.member.dto;


import com.yeminjilim.ssafyworld.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder(access = AccessLevel.PRIVATE)
@Data
public class MemberDTO {

    private Long id;
    private String sub;
    private String provider;
    private Long groupInfoId;
    private String name;
    private String serialNumber;

    public Member toEntity() {
        return Member.builder()
                .id(id)
                .sub(sub)
                .provider(provider)
                .groupInfoId(groupInfoId)
                .name(name)
                .serialNumber(serialNumber)
                .build();
    }

    public MemberDTO toDTO(Member member) {
        return MemberDTO.builder()
                .id(member.getId())
                .sub(member.getSub())
                .provider(member.getProvider())
                .groupInfoId(member.getGroupInfoId())
                .name(member.getName())
                .serialNumber(member.getSerialNumber())
                .build();
    }

}
