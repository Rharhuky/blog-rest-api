package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Post;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository){

        this.postRepository = postRepository;

    }

    @Override
    public PostDto createPost(PostDto postDto) {
        //convert DTo to Entity

        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());

        Post newPost = this.postRepository.save(post);

        //convert entity to DTO

        PostDto postResponse = new PostDto();
        postResponse.setId(newPost.getId());
        postResponse.setContent(newPost.getContent());
        postResponse.setTitle(newPost.getTitle());
        postResponse.setDescription(newPost.getDescription());

        return postResponse;
    }
}