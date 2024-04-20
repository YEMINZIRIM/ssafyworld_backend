package com.yeminjilim.ssafyworld.member.entity;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class Member {

    private MemberInfo memberInfo;
    private GroupInfo groupInfo;

    public Member(MemberInfo memberInfo, GroupInfo groupInfo) {
        this.memberInfo = memberInfo;
        this.groupInfo = groupInfo;
    }


}
