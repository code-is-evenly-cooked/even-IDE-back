package com.evenly.evenide.service;

import com.evenly.evenide.dto.SignUpDto;
import com.evenly.evenide.entity.User;
import com.evenly.evenide.global.exception.CustomException;
import com.evenly.evenide.global.exception.ErrorCode;
import com.evenly.evenide.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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


}
