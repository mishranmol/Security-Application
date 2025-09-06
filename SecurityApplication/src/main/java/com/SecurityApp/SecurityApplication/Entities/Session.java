package com.SecurityApp.SecurityApplication.Entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Table(name = "session_table")
@Entity
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //this is the sessionId
    private Long id;

    // @CreationTimestamp
    // The @CreationTimestamp annotation in Hibernate is used on entity fields to automatically capture the creation
    // date/time once when the entity is first persisted.
    // There is no need to put lastUsedAt inside constructor as @CreationTimestamp will initialize lastUsedAt when this entity will be
    // first persisted/inserted inside the table .
    @CreationTimestamp
    private LocalDateTime lastUsedAt;

    private String refreshToken ;

    @ManyToOne
    //since in our application one user can have many sessions(2 in our application) that's why ManytoOne
    private User user;


    public Session( User user , String refreshToken) {
        this.refreshToken = refreshToken;
        this.user = user;
    }

    //default constructor
    public Session(){}


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getLastUsedAt() {
        return lastUsedAt;
    }

    public void setLastUsedAt(LocalDateTime lastUsedAt) {
        this.lastUsedAt = lastUsedAt;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
