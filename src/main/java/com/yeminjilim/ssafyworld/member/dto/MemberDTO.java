package com.yeminjilim.ssafyworld.member.dto;


import com.yeminjilim.ssafyworld.member.entity.MemberInfo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

@Builder(access = AccessLevel.PRIVATE)
@Data
public class MemberDTO {

    private Integer id;
    private String sub;
    private String provider;
    private Long groupInfoId;
    private String name;
    private String serialNumber;

    public MemberInfo toEntity() {
        return MemberInfo.builder()
                .id(id)
                .sub(sub)
                .provider(provider)
                .groupInfoId(groupInfoId)
                .name(name)
                .serialNumber(serialNumber)
                .build();
    }

    public MemberDTO toDTO(MemberInfo member) {
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
