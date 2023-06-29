package com.springboot.blog.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CommentDto {

    private long id;

    @NotEmpty(message = "email must be not null or not empty ")
    @Email
    private String email;

    @NotEmpty(message = "Name must be not null or empty ")
    private String name;

    @NotEmpty
    @Size(min = 10, message = "Comment body should has 10 characters")
    private String body;
}
