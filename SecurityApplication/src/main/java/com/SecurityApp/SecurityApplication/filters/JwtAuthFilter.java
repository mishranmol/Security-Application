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


    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver handlerExceptionResolver ;


    public JwtAuthFilter(JwtService jwtService, UserService userService, HandlerExceptionResolver handlerExceptionResolver) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

         try {

             final String requestTokenHeader = request.getHeader("Authorization");

             if (requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer")) {
                 filterChain.doFilter(request, response);
                 return;
             }


             String token = requestTokenHeader.split("Bearer ")[1];

             
             Long userId = jwtService.getUserIdFromToken(token);

             if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                 User user = userService.getUserFromId(userId);

                 UsernamePasswordAuthenticationToken authenticationToken =
                         new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                 SecurityContextHolder.getContext().setAuthentication(authenticationToken);

             }
             
             filterChain.doFilter(request, response);

         }catch (Exception e){
             //it takes 4 input arguments -> req,res,handler,exception
             //null indicates the handler
             handlerExceptionResolver.resolveException(request,response, null ,e);
         }

    }
}
