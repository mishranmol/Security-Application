package com.SecurityApp.SecurityApplication;

import com.SecurityApp.SecurityApplication.Entities.User;
import com.SecurityApp.SecurityApplication.Services.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class SecurityApplicationTests {

	private final JwtService jwtService;

	@Autowired
	SecurityApplicationTests(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Test
	void contextLoads() {

		User user = new User(4l , "anmol@gmail.com" , "anmol123" , "anmol");

		String token = jwtService.generateToken(user);

		//fetching UserId from JWT token
		System.out.println(jwtService.getUserIdFromToken(token));

		System.out.println(token);

	}

}
