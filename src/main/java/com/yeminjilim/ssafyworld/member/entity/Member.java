package com.yeminjilim.ssafyworld.member.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member")
public class Member {

    @Id
    @Column("id")
    private Long id;

    @Column("sub")
    private String sub;

    @Column("provider")
    private String provider;

    @Column("groupInfoId")
    private Long groupInfoId;

    @Column("name")
    private String name;

    @Column("serialNumber")
    private String serialNumber;

    @Builder.Default
    @Column("createdAt")
    @CreatedDate
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    @Column("updatedAt")
    @LastModifiedDate
    private LocalDateTime updatedAt = LocalDateTime.now();

}
