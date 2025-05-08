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
import com.evenly.evenide.global.oauth.OAuthClient;
import com.evenly.evenide.global.util.SocialNameGenerator;
import com.evenly.evenide.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final List<OAuthClient> oAuthClients;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final SocialNameGenerator socialNameGenerator;

    @Transactional
    public SignInResponse loginWithOAuth(String provider, String accessToken) {
        OAuthClient client = oAuthClients.stream()
                .filter(c -> c.getProviderName().equalsIgnoreCase(provider))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.UNSUPPORTED_PROVIDER));

        OAuthUserInfo userInfo = client.getUserInfo(accessToken);

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
