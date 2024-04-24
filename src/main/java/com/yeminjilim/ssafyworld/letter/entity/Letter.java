package com.yeminjilim.ssafyworld.letter.entity;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;

@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Letter {
    @Id
    private Long id;

    @Column("toUser")
    private Long toUser;

    @Column("fromUser")
    private Long fromUser;

    private String title;
    private String content;
    private Integer hidden;

    @Column("createdAt") //@TODO Review => save후 저장된 entity 값을 받아올 수 없음
    @CreatedDate
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column("updatedAt")
    @LastModifiedDate
    private LocalDateTime updatedAt = LocalDateTime.now();

    public static Letter of(Long toUser, Long fromUser, String title, String content) {
        Letter letter = new Letter();
        letter.toUser = toUser;
        letter.fromUser = fromUser;
        letter.title = title;
        letter.content = content;

        return letter;
    }
}
