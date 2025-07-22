package com.demoproject.bump.controllers;

import com.demoproject.bump.exceptions.TitleNotValidException;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ErrorHandlerController {

    @ExceptionHandler(value = IllegalArgumentException.class)
    private ResponseEntity<Map<String, Object>>
            IllegalArgumentHandler(IllegalArgumentException ex) {
        final var response = new HashMap<String, Object>();

        response.put("code", HttpStatus.BAD_REQUEST.value());
        response.put("status", HttpStatus.BAD_REQUEST.getReasonPhrase());
        response.put("message", ex.getMessage());

        return ResponseEntity.badRequest().body(response);
    }


    @ExceptionHandler(value = TitleNotValidException.class)
    private ResponseEntity<Map<String, Object>>
    titleNotValidException(TitleNotValidException ex) {
        final var response = new HashMap<String, Object>();

        response.put("code", HttpStatus.BAD_REQUEST.value());
        response.put("status", HttpStatus.BAD_REQUEST.getReasonPhrase());
        response.put("message", ex.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

}
