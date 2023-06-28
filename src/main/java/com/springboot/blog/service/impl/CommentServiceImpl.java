package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exceptions.BlogAPIException;
import com.springboot.blog.exceptions.ResourceNotFoundException;
import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {

        Comment comment = mapToEntity(commentDto);

        //retorna entidade post pelo id

        Post post = this.postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId)); // utilizadno method reference ? , mas preciso passar parametros

        //necessario agora, set o post no comment

        comment.setPost(post);

        //por fim, preciso persistir minha entidade comment:

        Comment newComment = commentRepository.save(comment);
        return mapToDTO(newComment);

    }

    @Override
    public List<CommentDto> getCommentsByPostId(long postId) {
        //recupera os comentarios pelo id do post
        List<Comment> comments = this.commentRepository.findByPostId(postId);
        //retorna-os
        return comments.stream().map(this::mapToDTO).toList();

    }

    @Override
    public CommentDto getCommentById(Long postId, Long commentId) {

        //recuperar a entidade-registro do post pelo id
        Post post = this.postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        //recuperar o comentario pelo seu id
        Comment comment = this.commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

        if(!comment.getPost().getId().equals(post.getId()))
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to this post");

        return mapToDTO(comment);
    }

    @Override
    public CommentDto updateComment(Long postId, Long commentId, CommentDto commentRequest) {
        //replicacao de codigo..getCommentById
        Post post = this.postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        Comment comment = this.commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

        if(!comment.getPost().getId().equals(post.getId()))
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to this post");

        //salvando o comentario

        comment.setName(commentRequest.getName());
        comment.setBody(commentRequest.getBody());
        comment.setEmail(commentRequest.getEmail());

        //salvando o novo comentario:
        Comment commentResponse = this.commentRepository.save(comment);

        return mapToDTO(commentResponse);

    }

    @Override
    public void deleteComment(Long postId, Long commentId) {

        Post post = this.postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        Comment comment = this.commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

        if(!comment.getPost().getId().equals(post.getId()))
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to this post");

        this.commentRepository.delete(comment);


    }

    private CommentDto mapToDTO(Comment comment){

        CommentDto commentDto = new CommentDto();
        commentDto.setEmail(comment.getEmail());
        commentDto.setBody(comment.getBody());
        commentDto.setName(comment.getName());
        commentDto.setId(comment.getId());

        return commentDto;

    }

    private Comment mapToEntity(CommentDto commentDto){

        Comment comment = new Comment();
        comment.setBody(commentDto.getBody());
        comment.setEmail(commentDto.getEmail());
        comment.setId(commentDto.getId());
        comment.setName(commentDto.getName());

        return comment;

    }
}
