package com.yeminjilim.ssafyworld.member.dto;

import com.yeminjilim.ssafyworld.member.entity.GroupInfo;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GroupInfoDTO {

    private Long id;
    private Long ordinal;
    private String region;
    private Long ban;

    public GroupInfo toEntity() {
        return GroupInfo.builder()
                .id(id)
                .ordinal(ordinal)
                .region(region)
                .ban(ban)
                .build();
    }

    public static GroupInfoDTO toDTO(GroupInfo groupInfo) {
        return builder()
                .id(groupInfo.getId())
                .ordinal(groupInfo.getOrdinal())
                .region(groupInfo.getRegion())
                .ban(groupInfo.getBan())
                .build();
    }

}
