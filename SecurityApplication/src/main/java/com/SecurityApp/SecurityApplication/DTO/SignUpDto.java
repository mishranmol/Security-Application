package com.SecurityApp.SecurityApplication.DTO;


import com.SecurityApp.SecurityApplication.enums.Role;
import lombok.Data;

import java.util.Set;

@Data
public class SignUpDto {


    private String email;

    private String password;

    private String name;

    //using Set Data structure to remove the duplicate .
    private Set<Role> roles ;


    public SignUpDto(String email, String password, String name, Set<Role> roles) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.roles = roles;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Role> getRoles() {
        return roles;
    }

}
