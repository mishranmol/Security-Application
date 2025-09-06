package com.SecurityApp.SecurityApplication.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


//added data for getters and setters
@Data
//these two constructors are required by jackson to convert json into PostDto class
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {

    private Long Id;

    private String title;

    private String description;
}
