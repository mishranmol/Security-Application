package com.SecurityApp.SecurityApplication.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {

    private Long Id;

    private String title;

    private String description;
}
