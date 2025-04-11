package com.evenly.evenide.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 사용 중인 이메일입니다."),
    NICKNAME_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 사용 중인 닉네임입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "가입되지 않은 사용자 입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 틀립니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "Refresh token이 만료되었습니다. 다시 로그인하세요."),

    PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "프로젝트를 찾을 수 없습니다."),
    PROJECT_OWNER_NOT_YOU(HttpStatus.UNAUTHORIZED,"프로젝트 주인이 아닙니다."),
    FILE_NOT_FOUND(HttpStatus.UNAUTHORIZED,"코드 파일을 찾을 수 없습니다."),
    FILE_IS_LOCKED(HttpStatus.UNAUTHORIZED,"잠겨있는 파일입니다. 파일 주인만 열람이 가능합니다."),
    CODE_EDIT_LOCKED(HttpStatus.UNAUTHORIZED,"코드 수정이 잠겨있습니다. 파일 주인만 수정이 가능합니다."),

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다.");




    private final HttpStatus httpStatus;
    private String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
