package com.springboot.blog.payload;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class PostDto {

    private Long id;

    @NotNull
    @NotEmpty
    @Size(min = 5, message = "Post title must have 5 characters ")
    private String title;

    @NotNull
    @NotEmpty
    @Size(min = 10, message = "Post description must has 10 characters ")
    private String description;

    @NotEmpty
    private String content;


    private Set<CommentDto> comments;


}
