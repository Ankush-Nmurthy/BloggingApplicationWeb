package com.blogsculpture.exceptions;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NoHandlerFoundException.class)
    public String globalNoHandlerFoundExceptionsHandler(Model model, NoHandlerFoundException ne) {
        model.addAttribute("message", ne.getMessage());
        return "error";
    }

    @ExceptionHandler(NumberFormatException.class)
    public String globalNumberFormatterExceptionHandler(Model model, NumberFormatException ne) {
        model.addAttribute("message", ne.getMessage());
        return "error";
    }

    @ExceptionHandler(BlogExceptions.class)
    public String globalBlogExceptionHandler(Model model, BlogExceptions be) {
        model.addAttribute("message", be.getMessage());
        return "error";
    }
}
