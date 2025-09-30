package com.rentzy.service.impl;

import com.rentzy.converter.PostConverter;
import com.rentzy.entity.PostEntity;
import com.rentzy.enums.PostStatus;
import com.rentzy.model.dto.request.PostRequestDTO;
import com.rentzy.model.dto.response.PostResponseDTO;
import com.rentzy.repository.PostRepository;
import com.rentzy.service.PostService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostConverter postConverter;

    @Override
    public Page<PostEntity> searchPosts(PostRequestDTO criteria, Pageable pageable) {
        return postRepository.findByFilters(criteria, pageable);
    }

    @Override
    public Page<PostEntity> getPostsByStatus(PostStatus status, Pageable pageable) {
        return postRepository.findByStatus(status, pageable);
    }

    @Override
    public Page<PostEntity> getLatestPosts(Pageable pageable) {
        return postRepository.findByStatusOrderByCreatedAtDesc(PostStatus.APPROVED, pageable);
    }

    @Override
    public Page<PostEntity> getPostsByPriceRange(double minPrice, double maxPrice, Pageable pageable) {
        return postRepository.findByStatusAndPriceBetween(PostStatus.APPROVED, minPrice, maxPrice, pageable);
    }

    @Override
    public Page<PostEntity> getPostsByUser(String userId, Pageable pageable) {
        return postRepository.findByStatusAndCreatedBy(PostStatus.APPROVED, userId, pageable);
    }

    @Override
    public Page<PostEntity> getAllPostsByUser(String userId, Pageable pageable) {
        return postRepository.findByCreatedBy(userId, pageable);
    }

    @Override
    public Optional<PostEntity> getPostById(String id) {
        return postRepository.findById(id);
    }

    @Override
    @Transactional
    public PostResponseDTO createPost(PostEntity post, String userId) {
        post.setCreatedBy(userId);
        post.setStatus(PostStatus.PENDING);

        postRepository.save(post);

        return postConverter.toDTO(post);
    }

    @Override
    public PostResponseDTO updatePost(String id, PostRequestDTO updatedPost, String userId) {
        Optional<PostEntity> post = postRepository.findById(id);

        if (post.isEmpty()){
            throw new RuntimeException("Post not found with id: " + id);
        }

        PostEntity existingPost = post.get();
        if (!existingPost.getCreatedBy().equals(userId)) {
            throw new RuntimeException("You don't have permission to update this post");
        }

        existingPost.setPropertyName(updatedPost.getPropertyName());
        existingPost.setTitle(updatedPost.getTitle());
        existingPost.setDescription(updatedPost.getDescription());
        existingPost.setPrice(updatedPost.getPrice());
        existingPost.setImages(updatedPost.getImages());
        existingPost.setUtilities(updatedPost.getUtilities());

        existingPost.setStatus(PostStatus.PENDING);

        PostEntity savedPost = postRepository.save(existingPost);
        return postConverter.toDTO(savedPost);
    }

    @Override
    public void deletePost(String id, String userId) {
        Optional<PostEntity> existingPost = postRepository.findById(id);

        if (existingPost.isEmpty()){
            throw new RuntimeException("Post not found with id: " + id);
        }

        PostEntity post = existingPost.get();
        if (!post.getCreatedBy().equals(userId)) {
            throw new RuntimeException("You don't have permission to delete this post");
        }

        postRepository.deleteById(id);
    }

    @Override
    public PostResponseDTO approvePost(String id) {
        Optional<PostEntity> existingPost = postRepository.findById(id);
        if (existingPost.isEmpty()){
            throw new RuntimeException("Post not found with id: " + id);
        }

        PostEntity post = existingPost.get();
        post.setStatus(PostStatus.APPROVED);

        postRepository.save(post);
        return postConverter.toDTO(post);
    }

    @Override
    public PostResponseDTO rejectPost(String id) {
        Optional<PostEntity> existingPost = postRepository.findById(id);
        if (existingPost.isEmpty()){
            throw new RuntimeException("Post not found with id: " + id);
        }

        PostEntity post = existingPost.get();
        post.setStatus(PostStatus.REJECTED);
        postRepository.save(post);
        return postConverter.toDTO(post);
    }

    @Override
    public Page<PostEntity> getPendingPosts(Pageable pageable) {
        return postRepository.findByStatus(PostStatus.PENDING, pageable);
    }

    @Override
    public long countPostsByStatus(PostStatus status) {
        return postRepository.countByStatus(status);
    }
}
