package com.example.demo.myrestfulservice.exception;

// 사용자가 제어할 수 있는 형태는 exception이여야 한다.

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
