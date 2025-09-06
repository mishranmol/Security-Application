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
//we were having a circular dependency when we have written login method code inside the UserService hence we are writing login method here

// authController
//|     ↓
//|  userService
//↑     ↓
//|  authenticationManager

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

        //authenticationManager is a Functional Interface means it contains only one abstract method which is = "authenticate" method .
        //authenticationManager has only one abstract method named "authenticate" and this method takes an authentication object as input .
        // -> new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword() ) is the authentication object .
        //authenticate method has many implementation of it so we are using one of its implementation named UsernamePasswordAuthenticationToken and do
        //do ctrl+shift+click on authenticate and then goto green button on line->12 to know about who all implement this authentication .
        //The authenticationManager will return us an Authentication .

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword())
        );

        //do ctrl+shift+click on "authentication" to know about what all methods do authentication have.
        //note -> The "authentication" contains user after authenticating the request and the user is present inside the getPrinciple();
        User user = (User) authentication.getPrincipal();

        // return the JWT token to the user in response if the user is authenticated
        // before lecture 6.1 we were returning only JWT token to the user directly as shown below
        // return jwtService.generateRefreshToken(user);


        // now in lecture 6.1 we have to return two tokens( 1-> AccessToken(i.e JWT token) and 2->Refresh token ) to the user
        // for returning two things together we are making a LoginResponseDto
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        //creating a session for the user loggedIn
        sessionService.generateNewSession(user,refreshToken);

        return new LoginResponseDto(user.getId(), accessToken , refreshToken );

    }

    public LoginResponseDto refreshToken(String refreshToken) {

        //1st check whether the refreshToken is valid or not hence use method getUserIdFromToken() because if the refreshToken(RT) is
        //valid and not expired then we will be able to get userId from RT as we have set the userId inside the subject portion of refreshToken
        //but if the refreshToken is expired/invalid then it will throw exception(and we have handled the JwtException inside the
        // GlobalExceptionHandler) and we won't be able to get the userId from refreshToken .

        //if refreshToken is not expired then we will be able to fetch the userId from the user .
       Long userId = jwtService.getUserIdFromToken(refreshToken);

        // below line is implemented inside lecture 6.3
        // before refreshing the AccessToken(AT) using RT , first validate the refreshToken(RT) that whether the refreshToken
        // is present inside the DB or not .
        sessionService.validateSession(refreshToken);

       User user = userService.getUserFromId(userId);

       String accessToken = jwtService.generateAccessToken(user);

       return new LoginResponseDto(userId , accessToken , refreshToken) ;

    }

}
