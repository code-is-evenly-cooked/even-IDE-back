package com.evenly.evenide.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GoogleUserInfo implements OAuthUserInfo {

    private String sub;      // 고유 ID
    private String email;
    private String name;

    @Override
    public String getId() {
        return sub;
    }

    @Override
    public String getEmail() {
        return email;
    }
}
