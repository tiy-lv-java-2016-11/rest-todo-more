package com.theironyard.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by darionmoore on 1/10/17.
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "That List Does Not Exist")
public class ToDoException extends RuntimeException{
}
