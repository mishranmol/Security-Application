package com.SecurityApp.SecurityApplication.Services;

import com.SecurityApp.SecurityApplication.Entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Set;

@Service
public class JwtService {

           
           @Value("${jwt.secretKey}")
           private String secretKey;
           
           
           public SecretKey getSecretKey() {
               return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
           }

            //Method to generate JWT
            public String generateAccessToken(User user){

             return Jwts.builder()
                    .setSubject(user.getId().toString())
                    .claim("email" , user.getEmail())
                    .claim( "roles" , user.getRoles().toString())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 1000*60))
                    .signWith(getSecretKey())
                     .compact();

        }

        public String generateRefreshToken(User user) {

            return Jwts.builder()
                    .setSubject(user.getId().toString())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 1000*60*2))
                    .signWith(getSecretKey())
                    .compact();
        }



    public Long getUserIdFromToken(String token) {

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Long.valueOf(claims.getSubject());
    }

}
