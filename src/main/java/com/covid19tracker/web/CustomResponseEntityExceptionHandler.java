package com.covid19tracker.web;

import com.covid19tracker.web.models.GenericResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, ObjectError::getDefaultMessage));
        log.error("validation of arguments failed, errors: {}",errors);
        return ResponseEntity.badRequest().body(new GenericResponse<>(null,errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex){
        Map<String,String> errors = new HashMap<>();
        log.error("Exception occurred in application",ex);
        errors.put("generic_error","internal server exeption occurred, please contact administrator for resolution");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new GenericResponse<>(null,errors));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(Exception ex){
        Map<String,String> errors = new HashMap<>();
        log.error("Exception occurred in application",ex);
        errors.put("generic_error","You are not authorized to perform this operation");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new GenericResponse<>(null,errors));
    }
}
