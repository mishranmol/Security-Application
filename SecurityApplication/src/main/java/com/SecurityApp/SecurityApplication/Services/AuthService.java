package com.SecurityApp.SecurityApplication.Services;

import com.SecurityApp.SecurityApplication.DTO.LoginDto;
import com.SecurityApp.SecurityApplication.DTO.LoginResponseDto;
import com.SecurityApp.SecurityApplication.Entities.Session;
import com.SecurityApp.SecurityApplication.Entities.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final SessionService sessionService;

    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService, UserService userService, SessionService sessionService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
        this.sessionService = sessionService;
    }


    public LoginResponseDto login(LoginDto loginDto){
        
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword())
        );
   
        User user = (User) authentication.getPrincipal();

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        //creating a session for the user loggedIn
        sessionService.generateNewSession(user,refreshToken);

        return new LoginResponseDto(user.getId(), accessToken , refreshToken );

    }

    public LoginResponseDto refreshToken(String refreshToken) {

       Long userId = jwtService.getUserIdFromToken(refreshToken);

        sessionService.validateSession(refreshToken);

       User user = userService.getUserFromId(userId);

       String accessToken = jwtService.generateAccessToken(user);

       return new LoginResponseDto(userId , accessToken , refreshToken) ;

    }

}
