package com.theironyard.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by sparatan117 on 1/10/17.
 */
@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "login failed")
public class LoginFailedException extends RuntimeException{
}
