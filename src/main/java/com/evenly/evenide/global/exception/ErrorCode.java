package com.evenly.evenide.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 사용 중인 이메일입니다."),
    NICKNAME_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 사용 중인 닉네임입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "가입되지 않은 사용자 입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "Refresh token이 만료되었습니다. 다시 로그인하세요."),
    INVALID_PASSWORD_RESET_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 비밀번호 재설정 토큰입니다."),
    EXPIRED_PASSWORD_RESET_TOKEN(HttpStatus.BAD_REQUEST, "만료된 비밀번호 재설정 토큰입니다."),

    PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "프로젝트를 찾을 수 없습니다."),
    PROJECT_OWNER_NOT_YOU(HttpStatus.FORBIDDEN,"프로젝트 주인이 아닙니다."),
    INVALID_PROJECT_ACCESS(HttpStatus.FORBIDDEN, "파일이 프로젝트 내에 없습니다."),
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND,"파일을 찾을 수 없습니다."),
    FILE_IS_LOCKED(HttpStatus.FORBIDDEN,"잠겨있는 파일입니다. 파일 주인만 열람이 가능합니다."),
    CODE_EDIT_LOCKED(HttpStatus.FORBIDDEN,"코드 수정이 잠겨있습니다. 파일 주인만 수정이 가능합니다."),

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "로그인 인증이 필요합니다."),
    SAME_PASSWORD(HttpStatus.BAD_REQUEST, "기존 비밀번호와 새 비밀번호가 동일합니다."),

    INVALID_OAUTH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 소셜 인증 토큰입니다."),
    UNSUPPORTED_PROVIDER(HttpStatus.UNAUTHORIZED, "지원하지 않는 소셜 로그인 방식입니다.");




    private final HttpStatus httpStatus;
    private String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
