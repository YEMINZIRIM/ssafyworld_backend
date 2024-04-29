package com.yeminjilim.ssafyworld.letter.dto;

import com.yeminjilim.ssafyworld.letter.entity.Letter;
import com.yeminjilim.ssafyworld.member.entity.GroupInfo;
import com.yeminjilim.ssafyworld.member.entity.MemberInfo;
import com.yeminjilim.ssafyworld.util.TimeFormatUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

public class LetterDTO {
    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
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

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    public static class HideRequest {
        private Long letterId;
        private boolean hidden;
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ReceivedLetterResponse {
        private Long letterId;
        private String title;
        private String content;
        private Boolean hidden;
        private LocalDateTime createdAt;

        public static ReceivedLetterResponse of(Letter letter) {
            return new ReceivedLetterResponse(letter.getId(), letter.getTitle(), letter.getContent(), letter.getHidden(), letter.getCreatedAt());
        }
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class SentLetterResponse {
        private Long letterId;
        private Long toUserOrdinal;
        private String toUserRegion;
        private Long toUserBan;
        private String toUserName;
        private String title;
        private String content;
        private LocalDateTime createdAt;

        public static SentLetterResponse of(Letter letter, MemberInfo memberInfo, GroupInfo groupInfo) {
            return new SentLetterResponse(
                    letter.getId(),
                    groupInfo.getOrdinal(),
                    groupInfo.getRegion(),
                    groupInfo.getBan(),
                    memberInfo.getName(),
                    letter.getTitle(),
                    letter.getContent(),
                    letter.getCreatedAt());
        }
    }
}
