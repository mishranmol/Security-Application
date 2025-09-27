package com.SecurityApp.SecurityApplication.advices;


import com.SecurityApp.SecurityApplication.exceptions.ResourceNotFoundException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> ResourceNotFoundException(ResourceNotFoundException exception){

        ApiError apiError = new ApiError(exception.getMessage(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(apiError,HttpStatus.NOT_FOUND);


    }


    //Handling Spring Security exceptions
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuthenticationException(AuthenticationException e){

        ApiError apiError = new ApiError(e.getMessage() , HttpStatus.UNAUTHORIZED);

        return new ResponseEntity<>(apiError,HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiError> handleJwtException(JwtException e){

        ApiError apiError = new ApiError(e.getMessage() , HttpStatus.UNAUTHORIZED);

        return new ResponseEntity<>(apiError , HttpStatus.UNAUTHORIZED);

    }
}
