package com.example.todolist.exception;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException() {
        super("비밀번호가 일치하지 않습니다. 다시 확인해주세요.");
    }

    public InvalidPasswordException(String message) {
        super(message);
    }
}
