package com.springboot.blog.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.springboot.blog.entity.Post;
import com.springboot.blog.exceptions.ResourceNotFoundException;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;
    private ModelMapper mapper;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, ModelMapper mapper){

        this.postRepository = postRepository;
        this.mapper = mapper;

    }

    @Override
    public PostDto createPost(PostDto postDto) {
        //convert DTo to Entity

        Post post = mapToEntity(postDto);
        Post newPost = this.postRepository.save(post);

        //convert entity to DTO
        PostDto postResponse = mapToDTO(newPost);

        return postResponse;
    }

    //Entity para DTO
    private PostDto mapToDTO(Post post){

        PostDto postResponse = this.mapper.map(post, PostDto.class);
//        postResponse.setId(post.getId());
//        postResponse.setContent(post.getContent());
//        postResponse.setTitle(post.getTitle());
//        postResponse.setDescription(post.getDescription());

        return postResponse;
    }

    //DTo para entity
    private Post mapToEntity(PostDto postDto){

        Post postReponse = this.mapper.map(postDto, Post.class);
//        postReponse.setContent(postDto.getContent());
//        postReponse.setId(postDto.getId());
//        postReponse.setDescription(postDto.getDescription());
//        postReponse.setTitle(postDto.getTitle());

        return postReponse;
    }

    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {

//        List<PostDto> postsResponse = new ArrayList<>();
//        Pageable pageable = PageRequest.of(pageNo, pageSize);
//
//        this.postRepository.findAll(pageable).getContent().stream()
//                .map(this::mapToDTO)
//                .forEach(postsResponse::add);
//
//
//        return postsResponse;

        //create pageable instance


//        Pageable pageable = PageRequest.of(pageNo, pageSize); - apenas paginacao

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Post> posts = this.postRepository.findAll(pageable);

        // get content for page object
            //td vez q eu crio um tipo Page como objeto, é necessario o .getContent() no objeto page
        List<Post> listOfPosts = posts.getContent();

//        return listOfPosts.stream().map(this::mapToDTO).collect(Collectors.toList());

        List<PostDto> content = listOfPosts.stream().map(this::mapToDTO).toList();
        // criando instancia de PostResponse

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());

        return postResponse;
    }

    @Override
    public PostDto getPostById(long postId) {

        Post post = this.postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        return mapToDTO(post);
    }

    @Override
    public PostDto updatePost(PostDto postDto, long postId) {

        Post post = this.postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());

        Post updatePost = this.postRepository.save(post);
        return mapToDTO(updatePost);

    }

    @Override
    public void deletePostById(long id) {

        Post post = this.postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        this.postRepository.delete(post);
    }
}
