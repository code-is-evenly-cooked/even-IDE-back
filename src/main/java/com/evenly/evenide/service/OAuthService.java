package com.evenly.evenide.service;

import com.evenly.evenide.config.security.JwtUtil;
import com.evenly.evenide.dto.JwtUserInfoDto;
import com.evenly.evenide.dto.OAuthUserInfo;
import com.evenly.evenide.dto.SignInResponse;
import com.evenly.evenide.entity.User;
import com.evenly.evenide.global.exception.CustomException;
import com.evenly.evenide.global.exception.ErrorCode;
import com.evenly.evenide.global.oauth.GoogleOAuthClient;
import com.evenly.evenide.global.oauth.KakaoOAuthClient;
import com.evenly.evenide.global.util.SocialNameGenerator;
import com.evenly.evenide.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final GoogleOAuthClient googleOAuthClient;
    private final KakaoOAuthClient kakaoOAuthClient;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final SocialNameGenerator socialNameGenerator;

    @Transactional
    public SignInResponse loginWithOAuth(String provider, String accessToken) {
        OAuthUserInfo userInfo = switch (provider.toUpperCase()) {
            case "GOOGLE" -> googleOAuthClient.getUserInfo(accessToken);
            case "KAKAO" -> kakaoOAuthClient.getUserInfo(accessToken);
            default -> throw new CustomException(ErrorCode.UNSUPPORTED_PROVIDER);
        };

        return userRepository.findByProviderAndProviderId(provider, userInfo.getId())
                .map(this::createSignInResponse)
                .orElseGet(() -> {
                    String nickname = socialNameGenerator.generateUnique(provider, userRepository::existsByNickname);
                    User newUser = User.createSocialUser(
                            userInfo.getEmail(),
                            nickname,
                            provider,
                            userInfo.getId()
                    );
                    userRepository.save(newUser);
                    return createSignInResponse(newUser);
                });
    }

    private SignInResponse createSignInResponse(User user) {
        String[] tokens = jwtUtil.generateToken(new JwtUserInfoDto(user.getId().toString()));
        return new SignInResponse(tokens[0], tokens[1], user.getId(), user.getNickname(), user.getProvider());
    }


}
