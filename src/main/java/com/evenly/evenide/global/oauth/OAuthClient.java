package com.evenly.evenide.global.oauth;

import com.evenly.evenide.dto.OAuthUserInfo;

public interface OAuthClient {
    String getProviderName();
    OAuthUserInfo getUserInfo(String accessToken);
}
