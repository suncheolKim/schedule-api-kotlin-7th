package net.sckim.scheduleapi.exception;

public class PasswordMismatchedException extends RuntimeException {
    public PasswordMismatchedException() {
        super("비밀번호 불일치");
    }

}
