package com.SecurityApp.SecurityApplication.Controllers;

import com.SecurityApp.SecurityApplication.DTO.PostDto;
import com.SecurityApp.SecurityApplication.Services.PostService;
import com.SecurityApp.SecurityApplication.exceptions.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }


    @GetMapping(path = "/{postId}")
    public PostDto getPostById(@PathVariable Long postId) throws ResourceNotFoundException {
        return postService.getPostById(postId);
    }


    @GetMapping
    public List<PostDto> getAllPosts(){
        return postService.getAllPosts();
    }


    @PostMapping(path = "/newPost")
    public PostDto createNewPost(@RequestBody PostDto inputPost){
        return postService.createNewPost(inputPost);
    }


    @DeleteMapping(path = "/{postId}")
    public void DeletePostById(@PathVariable Long postId) throws ResourceNotFoundException {
         postService.DeletePostById(postId);
    }

}
