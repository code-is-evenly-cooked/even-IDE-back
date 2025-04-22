package com.evenly.evenide.global.oauth;

import com.evenly.evenide.dto.KakaoUserInfo;
import com.evenly.evenide.global.exception.CustomException;
import com.evenly.evenide.global.exception.ErrorCode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class KakaoOAuthClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String KAKAO_USERINFO_URL = "https://kapi.kakao.com/v2/user/me";

    public KakaoUserInfo getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<KakaoUserInfo> response = restTemplate.exchange(
                    KAKAO_USERINFO_URL,
                    HttpMethod.GET,
                    request,
                    KakaoUserInfo.class
            );
            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new CustomException(ErrorCode.INVALID_OAUTH_TOKEN);
        }
    }
}
