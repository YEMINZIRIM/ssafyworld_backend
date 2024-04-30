package com.yeminjilim.ssafyworld.letter.entity;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;

@ToString
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
    private Boolean hidden = false;

    @Column("createdAt")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column("updatedAt")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public static Letter of(Long toUser, Long fromUser, String title, String content) {
        Letter letter = new Letter();
        letter.toUser = toUser;
        letter.fromUser = fromUser;
        letter.title = title;
        letter.content = content;

        return letter;
    }

    public void hide(boolean b) {
        this.hidden = b;
    }
}
