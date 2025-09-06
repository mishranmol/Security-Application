package com.SecurityApp.SecurityApplication.advices;



import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class ApiError {

    private String error;

    private LocalDateTime timestamp;

    private HttpStatus status;

    public ApiError(){
        this.timestamp=LocalDateTime.now();
    }

    public ApiError(String error, HttpStatus status) {
        this();//calling the default constructor , this method known as constructor chaining
        this.error = error;
        this.status = status;
    }

}
