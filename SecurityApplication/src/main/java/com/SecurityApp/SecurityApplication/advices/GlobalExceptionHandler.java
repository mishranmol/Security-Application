package com.SecurityApp.SecurityApplication.advices;


import com.SecurityApp.SecurityApplication.exceptions.ResourceNotFoundException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//this is the annotation which looks for all the exceptions occurred
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> ResourceNotFoundException(ResourceNotFoundException exception){

        ApiError apiError = new ApiError(exception.getMessage(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(apiError,HttpStatus.NOT_FOUND);


    }



    //Handling Spring Security exceptions
    //Note -> We can handle all the exceptions which comes under AuthenticationException.class separately also but doing like below will also work.
    //Make sure it should be imported from springframework.security.core only.
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuthenticationException(AuthenticationException e){

        //note -> By default if we get any type of exception in SpringSecurity then SpringSecurity will give us 403(forbidden) error
        // which is not correct because 403(forbidden) means the user is not authorized .
        //forbidden error means the user is authenticated but he/she is not authorized to perform certain task .
        //for example -> if we try login with a wrong password then it will throw a 403(forbidden) error in postman which is not correct .
        //for example -> if we do login with a wrong email then also it will throw a 403(forbidden) error which is not correct instead it should show badCredentialException.
        //so to handle both the above mentioned example we are using HttpStatus.UNAUTHORIZED this will show us the correct status and meaning .

        // now after putting HttpStatus.UNAUTHORIZED when we enter wrong "email" we get 401(Unauthorized) and in response body it shows like ->
        //  "error": "User with the given email varun@gail.com did not exists",
        //  "timestamp": "2025-06-28T12:04:16.9687779",
        //  "status": "UNAUTHORIZED"
        //  it's showing "error": "User with the given email varun@gail.com did not exists", because we are handling BadCredentialException inside the UserService

        //now after putting HttpStatus.UNAUTHORIZED when we enter wrong "password" we get 401(Unauthorized) and in response body it shows like ->
        // "error": "Bad credentials",
        // "timestamp": "2025-06-28T12:07:48.7062261",
        // "status": "UNAUTHORIZED"

        ApiError apiError = new ApiError(e.getMessage() , HttpStatus.UNAUTHORIZED);

        return new ResponseEntity<>(apiError,HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiError> handleJwtException(JwtException e){

        ApiError apiError = new ApiError(e.getMessage() , HttpStatus.UNAUTHORIZED);

        return new ResponseEntity<>(apiError , HttpStatus.UNAUTHORIZED);

    }
}
