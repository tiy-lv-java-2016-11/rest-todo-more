package com.theironyard.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class LoginFailedException extends RuntimeException {
}
