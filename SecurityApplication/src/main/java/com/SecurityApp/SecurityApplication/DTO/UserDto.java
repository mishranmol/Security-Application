package com.SecurityApp.SecurityApplication.DTO;


public class UserDto {

    private Long Id;

    private String email;

    private String name;

    public UserDto(Long id, String email, String name) {
        Id = id;
        this.email = email;
        this.name = name;
    }

    public UserDto(){
    }


    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
