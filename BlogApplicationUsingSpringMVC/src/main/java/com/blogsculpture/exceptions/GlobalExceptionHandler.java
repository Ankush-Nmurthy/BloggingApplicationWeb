package com.blogsculpture.exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NoHandlerFoundException.class)
    public String getAllExceptions() {
        return "redirect:404";
    }

    @ExceptionHandler(NumberFormatException.class)
    public String getNumberFormatterExceptin(NumberFormatException ne){
        return "error";
    }

}
