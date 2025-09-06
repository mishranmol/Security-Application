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
    //we were having a circular dependency when we have written this login method code inside the UserService hence we are writing inside authService .
    //we are passing the HttpServletResponse object to save our token inside a cookie
    // login method inside AuthService
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto loginDto , HttpServletResponse response){

        // before lecture 6.1(refresh token and access token) we were getting only JWT(i.e->accessToken) in response but now in lecture 6.1
        // we are getting 2 tokens(accessToken and refreshToken) in response .
        // String token = authService.login(loginDto);


        //This loginResponseDto contains both the tokens(access and refresh)
        LoginResponseDto loginResponseDto = authService.login(loginDto);

        // before lecture 6.1 , creating a cookie to save the JWT(accessToken)
        // Cookie cookie = new Cookie("token" , token);

        //but now in lecture 6.1 , we have to save the refreshToken inside the cookie
        Cookie cookie = new Cookie("refreshToken" , loginResponseDto.getRefreshToken());

        // we can set a lot of information about this cookie .
        // we are doing setHttpOnly(true); so that this cookie cannot be accessed by any another means , and it can only be found with help of
        // HTTP methods and only the loggedIn user can have access to this cookie .
        // httpOnly cookies can be passed from the backend to the frontend .
        cookie.setHttpOnly(true);


        //adding the cookie as a part of response as this cookie will be containing our token .
        response.addCookie(cookie);

        //now we can see inside our postman that when we will be hitting the "/login" route then we will be getting a cookie also in response .
        return ResponseEntity.ok(loginResponseDto);
    }


    @PostMapping(path = "/refresh")
    public ResponseEntity<LoginResponseDto> refresh(HttpServletRequest request){

        //request.getCookies() return's array of cookies
        // so we will iterate over all the cookies and get the cookie whose name is "refreshToken"
        // Arrays.stream takes in array as input and convert it to stream
        // What .filter(...) does in a Stream ???
        //Stream<T> filter(Predicate<? super T> predicate)
        //Accepts a Predicate<T> (a lambda returning true or false for each item).
        //Returns a new stream containing only elements for which the predicate is true

        //example -> List<Integer> nums = List.of(1, 2, 3, 4, 5, 6);
        //nums.stream()
        //    .filter(n -> n % 2 == 0)  // keeps only even numbers
        //    .forEach(System.out::println);

        // cookie constructor contains 2 things -> name and value
        // findFirst() gives the first result
        // map is used to map the cookie to the value

      String refreshToken =  Arrays.stream(request.getCookies())
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(cookie -> cookie.getValue())
                .orElseThrow( () -> new AuthenticationServiceException("refresh token not found inside the cookie")) ;

        LoginResponseDto loginResponseDto = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(loginResponseDto);

    }

//    @PostMapping(path = "/logout")
//    public void logout(@RequestBody String refreshToken){
//        authService.logout(refreshToken);
//    }
//
//    public void logout(String refreshToken) {
//
//        Session session = sessionService.getSessionByRefreshToken(refreshToken);
//        sessionService.
//    }

    @DeleteMapping(path = "delete/{Id}")
    public void deleteUserById(@PathVariable Long Id) throws ResourceNotFoundException {
         userService.deleteUserById(Id);
    }
}
