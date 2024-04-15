package com.yeminjilim.ssafyworld.member.entity;

import io.r2dbc.spi.Row;
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
public class MemberInfo {

    @Id
    @Column("memberId")
    private Integer id;

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

    public static MemberInfo mapping(Row row) {
        return builder()
                .id(row.get("memberId",Integer.class))
                .sub(row.get("sub",String.class))
                .provider(row.get("provider",String.class))
                .groupInfoId(row.get("groupInfoId",Long.class))
                .name(row.get("name",String.class))
                .serialNumber(row.get("serialNumber",String.class))
                .createdAt(row.get("createdAt",LocalDateTime.class))
                .updatedAt(row.get("updatedAt",LocalDateTime.class))
                .build();
    }

}
