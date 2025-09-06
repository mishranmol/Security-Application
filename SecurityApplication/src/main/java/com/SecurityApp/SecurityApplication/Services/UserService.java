package com.SecurityApp.SecurityApplication.Services;

import com.SecurityApp.SecurityApplication.DTO.LoginDto;
import com.SecurityApp.SecurityApplication.DTO.SignUpDto;
import com.SecurityApp.SecurityApplication.DTO.UserDto;
import com.SecurityApp.SecurityApplication.Entities.User;
import com.SecurityApp.SecurityApplication.Repositories.UserRepository;
import com.SecurityApp.SecurityApplication.exceptions.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    //Injecting the passwordEncoder which we have created inside the AppConfig
   private final PasswordEncoder passwordEncoder ;

    public UserService(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;

    }


    @Override
    //BadCredentialsException comes under the AuthenticationException.class
    public UserDetails loadUserByUsername(String username) {
        return userRepository
                .findByEmail(username)
                .orElseThrow( ()-> new BadCredentialsException("User with the given email "+ username + " did not exists"));
    }


    //getting the user from userId
    public User getUserFromId(Long userId) {
        try {
            return userRepository.findById(userId).orElseThrow( () -> new ResourceNotFoundException("User with the given id" +  userId
                    + "did not exists") );
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public UserDto signUp(SignUpDto signUpDto){
            Optional<User> user = userRepository.findByEmail(signUpDto.getEmail());
             //means that if user is present already then user has to login and not signUp.
            if(user.isPresent()){
             throw new BadCredentialsException("User with the given email " + signUpDto.getEmail() + " already exists .");
        }
             User saveduser =  modelMapper.map(signUpDto,User.class);
            //we are encoding the password using passwordEncoder before storing it inside DB and after encoding the password
        //we will not be able to get back our original password again by any means
             saveduser.setPassword(passwordEncoder.encode(saveduser.getPassword()));
             return modelMapper.map(userRepository.save(saveduser) , UserDto.class);
    }


    //deleting a user by Id
    public void deleteUserById(Long id) throws ResourceNotFoundException {

        User user = userRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("user with given Id " +id +" did not exists"));
        userRepository.deleteById(user.getId());

    }
}
