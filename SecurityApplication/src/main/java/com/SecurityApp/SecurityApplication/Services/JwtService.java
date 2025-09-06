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
//inside this we will be having 2 methods 1st-> generate the token , 2nd -> to verify the token
public class JwtService {


           //note that this @Value is coming from beans.factory.annotation.Value and not from lombok
           @Value("${jwt.secretKey}")
           private String secretKey;

           //creating method to generate key as signWith needs a key as input
           public SecretKey getSecretKey() {
               return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
           }





           //method used to generate the JWT
           //make sure the user is coming from userEntity and not any other source.
           // token needs a user input
            public String generateAccessToken(User user){

            //builder is used to build the token
            //subject  is something used to identify the user
            //Id is Long so converting it to String using toString()
            //claims are key-value pairs and inside the claims we can add our data(payload) which we want to send
            //setIssuedAt tells the time at what the JWT is issued , each JWT must have issued time and expire time as well
            //setExpiration tells the time at which the token will expire
            //new Date() gives you the current date and time
            //1 sec -> 1000 Milliseconds , setting the expiration time of JWT after 10 mins
            //signWith needs a key Object as input
            // no such description about compact()

             return Jwts.builder()
                    .setSubject(user.getId().toString())
                    .claim("email" , user.getEmail())
                    .claim( "roles" , user.getRoles().toString())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 1000*60))
                    .signWith(getSecretKey())
                     .compact();

        }

        // we have build this below method( generateRefreshToken() ) in lecture 6.1(week-6)
        // we have removed claims from Refresh token because we don't want that Refresh token to act as access token .
        // 1 sec -> 1000 Milliseconds
        // setting the expiration time of Refresh token to 6 months -> 1000*60*60*24*30*6
        // We will do 1000L and not just 1000 as Numeric overflow in expression 1000 * 60 * 60 * 24 * 30 * 6: integer multiplication will occur
    // So will implicitly cast to long .

        public String generateRefreshToken(User user) {

            return Jwts.builder()
                    .setSubject(user.getId().toString())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 1000*60*2))
                    .signWith(getSecretKey())
                    .compact();
        }


//        how we can get userId from token as we are passing userid inside subject
//        public Long getUserIdFromToken(String token){
//
//           // first we have to decode the token so to decode
//            Claims claims = Jwts.parserBuilder()
//                    .verify(getSecretKey())
//                    .build()
//                    .parseSignedClaims(token)
//                    .getPayload();
//            return Long.valueOf(claims.getSubject());
//        }

    public Long getUserIdFromToken(String token) {
        //we are taking out the claims because claims contains the payload inside the JWT
        //for taking out the things from the token means parsing the token
        //then we will verify the token with the secretKey we have hence using .setSigningKey(getSecretKey()) but
        // in anuj bhaiya video he used  .verify(getSecretKey()) but this method is giving error when using

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        // Since we have stored the Id of the user inside the Subject(.setSubject(user.getId().toString()) ) when generating the token hence for fetching
        // will use .getSubject() .
        // Since the Id was stored in the form of String when we were creating the token so converting String to Long hence used Long.valueof();
        return Long.valueOf(claims.getSubject());
    }

}
