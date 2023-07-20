package com.springboot.blog.repository;

import com.springboot.blog.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {


    List<Comment> findByPostId(@Param("postId") long postId);

    //achei q precisasse usar uma query :
    /*
    @Query(value = "SELECT * FROM comments WHERE post_id=:postId",nativeQuery = true)
    pelo visto n precisa, o srping data se vira com isso...

        List<Comment> findByPostId(@Param("postId") long postId);
     */


}
