package com.SecurityApp.SecurityApplication.Controllers;

import com.SecurityApp.SecurityApplication.DTO.LoginResponseDto;
import com.SecurityApp.SecurityApplication.DTO.SignUpDto;
import com.SecurityApp.SecurityApplication.DTO.UserDto;
import com.SecurityApp.SecurityApplication.DTO.LoginDto;
import com.SecurityApp.SecurityApplication.Entities.Session;
import com.SecurityApp.SecurityApplication.Services.AuthService;
import com.SecurityApp.SecurityApplication.Services.UserService;
import com.SecurityApp.SecurityApplication.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping(path = "/auth")
public class AuthController {


    private final UserService userService;
    private final AuthService authService;


    public AuthController(UserService userService , AuthService authService) {
        this.userService = userService;
        this.authService=authService;
    }


    @PostMapping(path = "/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody SignUpDto signUpDto){
        UserDto userDto = userService.signUp(signUpDto);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping(path = "/login")
    // login method inside AuthService
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto loginDto , HttpServletResponse response){

        LoginResponseDto loginResponseDto = authService.login(loginDto);
        
        Cookie cookie = new Cookie("refreshToken" , loginResponseDto.getRefreshToken());

        // httpOnly cookies can be passed from the backend to the frontend .
        cookie.setHttpOnly(true);

        response.addCookie(cookie);

        return ResponseEntity.ok(loginResponseDto);
    }


    @PostMapping(path = "/refresh")
    public ResponseEntity<LoginResponseDto> refresh(HttpServletRequest request){

      String refreshToken =  Arrays.stream(request.getCookies())
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(cookie -> cookie.getValue())
                .orElseThrow( () -> new AuthenticationServiceException("refresh token not found inside the cookie")) ;

        LoginResponseDto loginResponseDto = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(loginResponseDto);

    }


    @DeleteMapping(path = "delete/{Id}")
    public void deleteUserById(@PathVariable Long Id) throws ResourceNotFoundException {
         userService.deleteUserById(Id);
    }
}
