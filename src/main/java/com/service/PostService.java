package com.service;

import com.model.dto.PostDTO;
import com.model.dto.PostSearchDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PostService {
    Page<PostDTO> getAllPosts(Pageable pageable);
    Optional<PostDTO> getPostById(String id);
    PostDTO createPost(PostDTO postDTO);
    PostDTO updatePost(String id, PostDTO postDTO);
    void deletePost(String id);
    Page<PostDTO> searchPosts(PostSearchDTO criteria, Pageable pageable);
}
