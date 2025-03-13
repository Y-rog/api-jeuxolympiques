package com.yrog.apijeuxolympiques.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<String> handleException(Exception e, HttpServletRequest request) {
        return new ResponseEntity<>("Une erreur est survenue lors de la création de l'évenement", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleInvalidDateFormat(HttpMessageNotReadableException ex) {
        String errorMessage = "Le format de la date est invalide. Utilisez le format dd/MM/yyyy HH:mm.";
        return new ResponseEntity<>(Map.of("error", errorMessage), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String errorMessage = "Une erreur d'intégrité des données est survenue";
        return new ResponseEntity<>(Map.of("error", errorMessage),  HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
