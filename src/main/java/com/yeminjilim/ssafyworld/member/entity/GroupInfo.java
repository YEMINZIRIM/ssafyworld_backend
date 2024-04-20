package com.yeminjilim.ssafyworld.member.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@AllArgsConstructor
@Data
@Table("group_info")
public class GroupInfo {

    @Id
    private Long id;

    @Column("ordinal")
    private Long ordinal;

    @Column("region")
    private String region;

    @Column("ban")
    private Long ban;


}
