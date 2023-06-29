package com.springboot.blog.controller;

import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.service.PostService;
import com.springboot.blog.utils.Constants;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/posts")
public class PostController {

    private PostService postService;
    public PostController(PostService postService){
        this.postService = postService;

    }

    //requisicao para criacao de um post no blog

    @PostMapping
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto){
        //to enable validade iin PostDto, i can use Valid annotation
        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);

    }

    //pageNo = numero da pagina
    //pageSize = tamanho da paginacao = como fatia as paginas;

    /*
    O que significa: da forma que foi feita, por "default" ele exibe 10 registros na pagina 0
    Isso implica que, na pagina 0, eu tenho 10 registros a se mostrar por padrão.
    Vamos dizer que eu tenha 13 registro no BD, entao se eu passar:
        posts?pageSize=1 , ele me retorna mais 3 registros(id: 11,12,13)

        Resumindo: pageNo : número da página (cada pagina retorna 10 registros por padrao - assim implementado
                   pageSize: qtd de registros.
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PostResponse getAllPosts(

            @RequestParam(value = "pageNo", defaultValue = Constants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = Constants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = Constants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = Constants.DEFAULT_SORT_DIRECTION, required = false)String sortDir) {

        return this.postService.getAllPosts(pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable("id") long postId){

        return new ResponseEntity<>(this.postService.getPostById(postId), HttpStatus.OK);

    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(@Valid @RequestBody PostDto postDto, @PathVariable long id){

        return  ResponseEntity.ok(this.postService.updatePost(postDto, id));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePostById(@PathVariable long id){
        this.postService.deletePostById(id);
        return new ResponseEntity<>("Post deleted sucessfully ", HttpStatus.OK);
    }

}
