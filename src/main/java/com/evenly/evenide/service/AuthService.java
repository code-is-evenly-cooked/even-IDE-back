package com.evenly.evenide.service;

import com.evenly.evenide.config.security.JwtUtil;
import com.evenly.evenide.dto.JwtUserInfoDto;
import com.evenly.evenide.dto.SignInDto;
import com.evenly.evenide.dto.SignInResponse;
import com.evenly.evenide.dto.SignUpDto;
import com.evenly.evenide.entity.PasswordResetToken;
import com.evenly.evenide.entity.RefreshToken;
import com.evenly.evenide.entity.User;
import com.evenly.evenide.global.exception.CustomException;
import com.evenly.evenide.global.exception.ErrorCode;
import com.evenly.evenide.repository.PasswordResetTokenRepository;
import com.evenly.evenide.repository.RefreshTokenRepository;
import com.evenly.evenide.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;

    @Value("${app.reset.url}")
    private String resetBaseUrl;

    public User signup(SignUpDto signUpDto) {

        // 이메일 중복 확인
        if (userRepository.existsByEmail(signUpDto.getEmail())) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        // 닉네임 중복 확인
        if (userRepository.existsByNickname(signUpDto.getNickname())) {
            throw new CustomException(ErrorCode.NICKNAME_ALREADY_EXISTS);
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(signUpDto.getPassword());

        User user = User.create(
                signUpDto.getEmail(),
                encodedPassword,
                signUpDto.getNickname()
        );

        return userRepository.save(user);
    }

    public void checkEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
    }

    public void checkNickname(String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new CustomException(ErrorCode.NICKNAME_ALREADY_EXISTS);
        }
    }

    // 로그인 부분
    @Transactional
    public SignInResponse login(SignInDto signInDto) {

        // 가입되지 않은 사용자 일때
        User user = userRepository.findByEmail(signInDto.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 비밀번호 검증 부분
        if (!passwordEncoder.matches(signInDto.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        // JWT Token 생성 부분
        JwtUserInfoDto userInfoDto = new JwtUserInfoDto(
                String.valueOf(user.getId())
        );
        String[] tokens = jwtUtil.generateToken(userInfoDto);
        String accessToken = tokens[0];
        String refreshToken = tokens[1];


        // RefreshToken 저장 및 업데이트
        refreshTokenRepository.findByUserId(user.getId())
                .ifPresentOrElse(
                        token -> token.updateToken(refreshToken),
                        ()-> refreshTokenRepository.save(
                        RefreshToken.builder()
                                .userId(user.getId())
                                .refreshToken(refreshToken)
                                .build()
                        )
                );
        return new SignInResponse(
                accessToken,
                refreshToken,
                user.getId(),
                user.getNickname(),
                user.getProvider()
        );
    }

    // refresh 부분
    public SignInResponse refresh(String refreshToken) {

        // 유효성 검사
        if (!jwtUtil.validateRefreshToken(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        // 사용자 ID 추출
        String userId = jwtUtil.getUserIdFromRefreshToken(refreshToken);

        // DB 사용자 조회
        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // DB <-> refreshToken 비교
        RefreshToken saved = refreshTokenRepository.findByUserId(user.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_REFRESH_TOKEN));

        if (!saved.getRefreshToken().equals(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        // 새 토큰 생성
        String newAccessToken = jwtUtil.renewAccessToken(refreshToken);
        String newRefreshToken = jwtUtil.generateToken(new JwtUserInfoDto(userId))[1];

        // RefreshToken 갱신
        saved.updateToken(newRefreshToken);

        return new SignInResponse(
                newAccessToken,
                newRefreshToken,
                user.getId(),
                user.getNickname(),
                user.getProvider()
        );
    }

    @Transactional
    public void logout(String token) {
        String userId = jwtUtil.getUserIdFromRefreshToken(token);
        refreshTokenRepository.deleteByUserId(Long.valueOf(userId));
    }

    @Transactional
    public User getUserInfo(String userId) {
        return userRepository.findById(Long.valueOf(userId))
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public void updatePassword(String userId, String currentPassword, String newPassword) {
        User user =  userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 현재 비밀번호 검증
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        // 현재 비밀번호 & 새 비밀번호 같은지 검증
        if (currentPassword.equals(newPassword)) {
            throw new CustomException(ErrorCode.SAME_PASSWORD);
        }

        // 새 비밀번호 저장
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.updatePassword(encodedPassword);

        refreshTokenRepository.deleteByUserId(user.getId());
    }

    // 비밀번호 재설정 - 이메일 전송 하는 부분
    @Transactional
    public void sendResetEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 기존 토큰 삭제
        passwordResetTokenRepository.deleteByUser(user);
        passwordResetTokenRepository.flush();
        // 토큰 생성
        String token = UUID.randomUUID().toString();

        // 토큰 저장
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .user(user)
                .token(token)
                .expiration(LocalDateTime.now().plusMinutes(10))
                .build();

        passwordResetTokenRepository.save(resetToken);

        String resetUrl = resetBaseUrl + "/password-reset?token=" + token;
        emailService.sendResetPasswordEmail(user.getEmail(), resetUrl);

    }

    // 비밀번호 재설정 - 링크 접속 후 비밀번호 변경하는 부분
    @Transactional
    public void resetPassword(String token, String newPassword) {

        // 토큰 없음
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_PASSWORD_RESET_TOKEN));

        // 토큰 만료
        if (resetToken.getExpiration().isBefore(LocalDateTime.now())) {
            throw new CustomException(ErrorCode.EXPIRED_PASSWORD_RESET_TOKEN);
        }

        User user = resetToken.getUser();
        user.updatePassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        passwordResetTokenRepository.delete(resetToken);
    }



}
