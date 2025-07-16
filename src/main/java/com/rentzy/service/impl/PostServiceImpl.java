package com.rentzy.service.impl;

import com.rentzy.converter.PostConverter;
import com.rentzy.exception.ResourceNotFoundException;
import com.rentzy.model.dto.PostDTO;
import com.rentzy.model.dto.PostSearchDTO;
import com.rentzy.entity.PostEntity;
import com.rentzy.repository.PostRepository;
import com.rentzy.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostConverter postConverter;

    @Override
    public Page<PostDTO> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable).map(postConverter::toDTO);
    }

    @Override
    public Optional<PostDTO> getPostById(String id) {
        return postRepository.findById(id).map(postConverter::toDTO);
    }

    @Override
    public PostDTO createPost(PostDTO postDTO) {
        PostEntity postEntity = postConverter.toEntity(postDTO);
        PostEntity savedEntity = postRepository.save(postEntity);
        return postConverter.toDTO(savedEntity);
    }

    @Override
    public PostDTO updatePost(String id, PostDTO postDTO) {
        PostEntity existingPost = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));
        postConverter.updateEntityFromDTO(postDTO, existingPost);
        PostEntity savedEntity = postRepository.save(existingPost);
        return postConverter.toDTO(savedEntity);
    }

    @Override
    public void deletePost(String id) {
        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));
        postRepository.delete(postEntity);
    }

    @Override
    public Page<PostDTO> searchPosts(PostSearchDTO criteria, Pageable pageable) {
        Page<PostEntity> postEntities = postRepository.findByFilters(criteria, pageable);
        return postEntities.map(postConverter::toDTO);
    }
}
