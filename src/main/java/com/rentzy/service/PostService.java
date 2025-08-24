package com.rentzy.service;

import com.rentzy.entity.PostEntity;
import com.rentzy.enums.PostStatus;
import com.rentzy.model.dto.request.PostRequestDTO;
import com.rentzy.model.dto.response.PostResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PostService {
    Page<PostEntity> searchPosts(PostRequestDTO criteria, Pageable pageable);
    Page<PostEntity> getPostsByStatus(PostStatus status, Pageable pageable);
    Page<PostEntity> getLatestPosts(Pageable pageable);
    Page<PostEntity> getPostsByPriceRange(double minPrice, double maxPrice, Pageable pageable);
    Page<PostEntity> getPostsByUser(String userId, Pageable pageable);
    Page<PostEntity> getAllPostsByUser(String userId, Pageable pageable);
    Optional<PostEntity> getPostById(String id);
    PostResponseDTO createPost(PostEntity post, String userId);
    PostResponseDTO updatePost(String id, PostRequestDTO updatedPost, String userId);
    void deletePost(String id, String userId);
    PostResponseDTO approvePost(String id);
    PostResponseDTO rejectPost(String id);
    Page<PostEntity> getPendingPosts(Pageable pageable);
    long countPostsByStatus(PostStatus status);
}
