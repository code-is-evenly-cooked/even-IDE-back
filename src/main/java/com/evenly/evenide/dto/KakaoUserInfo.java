package com.evenly.evenide.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoUserInfo implements OAuthUserInfo {

    private Long id;
    private KakaoAccount kakao_account;

    @Override
    public String getId() {
        return String.valueOf(id);
    }

    @Override
    public String getEmail() {
        return kakao_account != null ? kakao_account.getEmail() : null;
    }

    @Getter
    @NoArgsConstructor
    public static class KakaoAccount {
        private String email;
    }

    @Getter
    @NoArgsConstructor
    public static class Profile {
        private String nickname;
    }
}
