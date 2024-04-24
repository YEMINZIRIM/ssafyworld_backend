package com.yeminjilim.ssafyworld.letter.dto;

import com.yeminjilim.ssafyworld.letter.entity.Letter;
import com.yeminjilim.ssafyworld.util.TimeFormatUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class LetterDTO {
    @Getter
    public static class CreateRequest {
        private Long toUser;
        private String title;
        private String content;

        public static Letter toEntity(Long userId, CreateRequest request) {
            return Letter.of(request.toUser, userId, request.title, request.content);
        }
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class CreateResponse {
        private Long toUser;
        private String title;
        private String content;
        private String createdAt;

        public static CreateResponse of (Letter letter) {
            return new CreateResponse(letter.getToUser(), letter.getTitle(), letter.getContent(), TimeFormatUtil.parse(letter.getCreatedAt()));
        }
    }
}
