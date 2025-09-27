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
@EnableWebSecurity
public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public WebSecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .authorizeHttpRequests
                        (auth -> auth
                                .requestMatchers( "/error" , "/auth/**" , "/home.html" ).permitAll()
                                .requestMatchers("/posts/**").hasRole(ADMIN.name())
                                .anyRequest().authenticated())
                                .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable())
                                .addFilterBefore(jwtAuthFilter , UsernamePasswordAuthenticationFilter.class);
//                                .formLogin(Customizer.withDefaults());

        return httpSecurity.build();
    }


    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}
