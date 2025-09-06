package com.SecurityApp.SecurityApplication.Entities;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "posts")

// these 2 constructors are also needed by hibernate to convert entity into table
@NoArgsConstructor
@AllArgsConstructor
//to convert the DTO back to entity we have to use getter and setter
@Getter
@Setter
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String title;

    private String description;

}