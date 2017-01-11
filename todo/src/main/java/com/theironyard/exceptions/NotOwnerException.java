package com.theironyard.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by sparatan117 on 1/11/17.
 */
@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class NotOwnerException extends RuntimeException{
}
