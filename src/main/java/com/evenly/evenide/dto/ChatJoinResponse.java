package com.evenly.evenide.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatJoinResponse {
    private Long projectId;
    private String sender;
    private String nickname;

    private ChatPath chat;
    private EditorPath editor;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatPath {
        String subscribeTopic;
        String sendJoinPath;
        String sendMessagePath;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EditorPath {
        private Long fileId;
        private DiffPath diff;
        private CursorPath cursor;

        @Getter
        @AllArgsConstructor
        @NoArgsConstructor
        public static class DiffPath {
            private String subscribe;
            private String send;
        }

        @Getter
        @AllArgsConstructor
        @NoArgsConstructor
        public static class CursorPath {
            private String subscribe;
            private String send;
        }
    }
}
