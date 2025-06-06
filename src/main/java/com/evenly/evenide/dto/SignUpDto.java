package com.evenly.evenide.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDto {

    @Email(message = "올바른 이메일을 입력해주세요.")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "올바른 이메일 형식을 입력해주세요."
    )
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[_!@#$%^&*])[A-Za-z\\d_!@#$%^&*]{6,}$",
            message ="비밀번호는 대문자, 소문자, 숫자, 특수문자를 포함한 6자 이상이어야 합니다.")
    private String password;

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣_-]+$", message = "닉네임은 영문, 한글, 숫자, -, _만 가능합니다.")
    private String nickname;
}
