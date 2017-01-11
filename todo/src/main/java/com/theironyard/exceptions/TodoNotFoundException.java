package com.theironyard.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by sparatan117 on 1/11/17.
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "todo not found")
public class TodoNotFoundException extends RuntimeException{
}
