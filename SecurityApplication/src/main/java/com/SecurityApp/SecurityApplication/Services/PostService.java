package com.SecurityApp.SecurityApplication.Services;

import com.SecurityApp.SecurityApplication.DTO.PostDto;
import com.SecurityApp.SecurityApplication.Entities.PostEntity;
import com.SecurityApp.SecurityApplication.Entities.User;
import com.SecurityApp.SecurityApplication.Repositories.PostRepository;
import com.SecurityApp.SecurityApplication.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PostService {


    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    public PostService(PostRepository postRepository, ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }


    public PostDto getPostById(@PathVariable Long postId) throws ResourceNotFoundException {
        
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        PostEntity postEntity = postRepository.
                findById(postId)
                .orElseThrow( () -> new ResourceNotFoundException("Resource not found with given id "+ postId));

        return modelMapper.map(postEntity,PostDto.class);

    }


    public List<PostDto> getAllPosts(){
        return postRepository.
                findAll()
                .stream()
                .map(postEntity -> modelMapper.map(postEntity,PostDto.class))
                .collect(Collectors.toList());
    }


    public PostDto createNewPost(PostDto inputPost){
        PostEntity postEntity = modelMapper.map(inputPost,PostEntity.class);
        return modelMapper.map(postRepository.save(postEntity),PostDto.class);
    }

    public void DeletePostById(@PathVariable Long postId) throws ResourceNotFoundException {
        if(postRepository.existsById(postId)) {
            postRepository.deleteById(postId);
            return;
        }else{
            throw new ResourceNotFoundException("Post with the given id = " + postId + " did not exists.");
        }
    }
}



