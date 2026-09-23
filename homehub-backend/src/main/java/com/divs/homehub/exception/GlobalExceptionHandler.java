package com.divs.homehub.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<String> handleDuplicateMailException(DuplicateEmailException ex){
        logger.warn("Duplicate email registration attempt", ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists!");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(
            MethodArgumentNotValidException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Invalid request data");
    }

    @ExceptionHandler(AlreadyFamilyMemberException.class)
    public ResponseEntity<String> handleAlreadyFamilyMemberException(AlreadyFamilyMemberException ex){
        logger.warn("Already a Family member for this invite code. User can not join again : ", ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Already a Family member for this invite code. User can not join again.");
    }

}
