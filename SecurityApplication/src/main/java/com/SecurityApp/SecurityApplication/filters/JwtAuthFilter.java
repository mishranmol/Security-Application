package com.SecurityApp.SecurityApplication.filters;

import com.SecurityApp.SecurityApplication.Entities.User;
import com.SecurityApp.SecurityApplication.Services.JwtService;
import com.SecurityApp.SecurityApplication.Services.UserService;
import com.SecurityApp.SecurityApplication.exceptions.ResourceNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;


@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;


   // In Spring, @Qualifier is used alongside @Autowired to resolve ambiguity when multiple beans of the
   // same type exist. It tells Spring exactly which bean to inject .
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver handlerExceptionResolver ;



    public JwtAuthFilter(JwtService jwtService, UserService userService, HandlerExceptionResolver handlerExceptionResolver) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }


    // We have wrapped our method inside try-catch to handle the exceptions like if we are not able to
    //get the JWT token then it will throw the exception and go to the catch block and many more exception
    // can occur as well so it will handle all those exceptions .

    //each of the filter inside the SecurityFilterChain has this doFilterInternal() method and this method comes from the OncePerRequestFilter .
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

         try {

             //Fetching the JWT token from request .
             //The request can contain multiple types of headers .
             //Headers are basically key-value pairs , and the key for our token would be "Authorization" .
             //The nomenclature for storing the token is "bearer JWTtoken"
             final String requestTokenHeader = request.getHeader("Authorization");


             //if we don't get JWT token inside the header
             if (requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer")) {
                 //move to the next filter inside the filterChain
                 filterChain.doFilter(request, response);
                 return;
             }


             //Extracting the JWT token as token is stored in way like "Bearer JWTtoken" so to get the JWTtoken we have to split using
             //"Bearer " so we will get an array of string where 0th index contains an empty String("") , and 1st index contains the JWT token
             // so take the 1st index value out of it .
             String token = requestTokenHeader.split("Bearer ")[1];


             //taking the userId from the token
             Long userId = jwtService.getUserIdFromToken(token);



             //We are making sure that the authentication object is not present inside the SecurityContextHolder already before that
             // is why we are using ...getAuthentication() == null
             if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                 User user = userService.getUserFromId(userId);

                 // creating an authentication
                 // putting user inside the UsernamePasswordAuthenticationToken and we can get this user by doing ->
                 // SecurityContextHolder.getContext().setAuthentication.getPrinciple();
                 UsernamePasswordAuthenticationToken authenticationToken =
                         new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());


// now adding the user inside the SecurityContextHolder So SecurityContextHolder takes in authentication object as input that
// is why we are creating an authentication and this authentication contains the user .
                 SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//now after adding user we can get the userdata(i.e the user information) anywhere inside my application from the SecurityContextHolder

             }
             //moving the request to the next filter inside the filterChain and it is a necessary step
             filterChain.doFilter(request, response);

             //now at the last add this custom filter inside the filterChain(i.e Go to WebSecurityConfig ) and after adding the filter before
             //UsernamePasswordAuthenticationFilter so now the UsernamePasswordAuthenticationFilter will see that the Authentication object is already
             //present inside SecurityContextHolder so it will move the request to the next filter in the filter chain and so on .


             // To test that whether this custom filter is working or not , goto WebSecurityConfig comment the hasRole("ADMIN") then
             // run the application and hit login API of any already registered user you will get the JWT token in response body and also inside
             // the cookie , copy that token make a new GET request like -> /post/1 without inserting the JWT token inside the header so now after hitting
             // GET api we will be getting a forbidden error because we have set inside the WebSecurity config that all the routes need to be authenticated
             // before accessing any routes but now put that token inside the header and now hit the same GET api you'll be able to access that GET route
             // because an authenticated user is accessing that route as we have put the JWT token of a registered user inside the header so now that user will
             // be able to access that route .
         }catch (Exception e){
             //it takes 4 input arguments -> req,res,handler,exception
             //null indicates the handler
             handlerExceptionResolver.resolveException(request,response, null ,e);
         }


    }
}
