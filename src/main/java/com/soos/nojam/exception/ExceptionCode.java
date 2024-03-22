package com.soos.nojam.exception;

public enum ExceptionCode {

    
    USER_NOT_FOUND(404, "해당 유저를 찾을 수 없습니다."),

    USER_EMAIL_NOT_MATCHED(401, "이메일이 일치하지 않습니다."),
    USER_PASSWORD_NOT_MATCHED(401, "비밀번호가 일치하지 않습니다."),
    USER_LOGIN_FAILED(401, "로그인에 실패했습니다."),
    USER_EXIST(409, "이미 존재하는 유저입니다."),
    USER_EMAIL_DUPLICATION(409, "이미 존재하는 이메일입니다."),
    USER_NICKNAME_DUPLICATION(409, "이미 존재하는 닉네임입니다.");

    private final int status;
    private final String message;

    ExceptionCode(final int status, final String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
