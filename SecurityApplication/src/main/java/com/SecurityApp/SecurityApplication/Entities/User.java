package com.SecurityApp.SecurityApplication.Entities;

import com.SecurityApp.SecurityApplication.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Table(name = "Userstable")
@Entity
//@Data is used for getter and setter But it's not working in my code that is why I am using external getter,setter and constructor
//@Data
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    //the below line means that the email should be unique so we are putting unique constraint
    @Column(unique = true)
    private String email;
    private String password;
    private String name;


    //Note roles are known as authorities in language of SpringSecurity
    //If we want that a user can have just a single role then use -> private Role role ;
    //But if we want a user can have multiple roles then use  -> private Set<Role> roles ;
    //Since Role is enum hence using @Enumerated and there are 2 ways of storing enums 1->ORDINAL(i.e -> Numbers(0,1,2) ) and 2-> String
    //Note -> By default EnumType is ORDINAL means
    //If we use EnumType.ORDINAL then our roles will be stored as USER is mapped to 0 , CREATOR mapped to 1 , ADMIN mapped to 2 means inside our DB
    //it will not show USER,CREATOR,ADMIN instead it will show 0,1,2 .
    //But since we have used EnumType.STRING hence it will be stored like USER,CREATOR,ADMIN .

    //AS we are storing Role inside a Set hence will use @ElementCollection
    //fetch = FetchType.EAGER means everytime we are fetching/getting a user we want to fetch the roles directly hence
    // EAGER but if want to fetch the roles Later On then use FetchType.LAZY .


    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles ;



    public User(Long id, String email, String password, String name,Set<Role> roles) {
        Id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.roles=roles;
    }

    public User(){
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }






    //implementing the methods of UserDetails

    //this getAuthorities will return a list of GrantedAuthority.
    //Authority means what kind of activity a user can perform .
    //GrantedAuthority is an interface and this GrantedAuthority is implemented by simpleGrantedAuthority .
    //.map(role -> GrantedAuthority()) means converting the role to simple GrantedAuthority
    //role.name() so the name is coming from the enum type .

    //Very Important -> Whenever we are passing the roles inside the SimpleGrantedAuthority we need to always add the role prefix(i.e-> "ROLE_") else
    // the code will not run even though we are adding correct requestMatchers inside the WebSecurityConfig .

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_"+role.name()))
                .collect(Collectors.toSet());

    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        //note -> username in our case is email
        return email;
    }


}
