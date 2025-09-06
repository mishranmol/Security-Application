package com.SecurityApp.SecurityApplication.config;

import com.SecurityApp.SecurityApplication.filters.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.SecurityApp.SecurityApplication.enums.Role.ADMIN;

@Configuration
//By adding this we are telling the SpringBoot that we want to configure the SecurityFilterChain that SpringBoot has.
@EnableWebSecurity
public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public WebSecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

// Note that when we define our WebSecurity and if we don't define our formLogin then our form will not work that is why its compulsory to define the form login
//formLogin(Customizer.withDefaults()); // by doing this it will open form with default configuration means the form will work in its default manner like it was working before.

// .authorizeHttpRequests(auth -> auth.anyRequest().authenticated()) -> after adding this line if we try to access any route then we will be redirected
// to the login page and the page will be asking for login credentials but if we don't add this line then we can access any route directly without entering
// the credentials inside the login page .

// .requestMatchers("/posts").permitAll() ---> It means that all the posts requests will be public and anyone can access the posts requests without authenticating themselves.

// .requestMatchers("/posts/**").permitAll() --->  These are the public routes , it means that we can access all the routes that comes under posts "without" entering
// our credentials inside the LoginForm .

// .requestMatchers("/posts/**").hasAnyRole("ADMIN") ---> It means that any route that comes under the posts will be accessed only by the user who has Role = "ADMIN" , mean to
// say that anyone can access "/posts" route without authenticating themselves since we have done -> requestMatchers("/posts).permitAll() BUT if we will try to access
// "/posts/1" then only the user having Role = "ADMIN" can access it .

//.csrf( httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable() )  --> by writing so we are removing/disabling the Csrf token that is present inside our form .



        httpSecurity
                .authorizeHttpRequests
                        (auth -> auth
                                .requestMatchers( "/error" , "/auth/**" , "/home.html" ).permitAll()
                                .requestMatchers("/posts/**").hasRole(ADMIN.name())
                                .anyRequest().authenticated())
                                .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable())
                                .addFilterBefore(jwtAuthFilter , UsernamePasswordAuthenticationFilter.class);
//                                .formLogin(Customizer.withDefaults());



        //we are returning the SecurityFilterChain
        return httpSecurity.build();
    }


    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}
