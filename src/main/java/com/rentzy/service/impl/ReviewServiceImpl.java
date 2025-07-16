package com.rentzy.service.impl;

import com.rentzy.converter.ReviewConverter;
import com.rentzy.exception.ResourceNotFoundException;
import com.rentzy.model.dto.ReviewDTO;
import com.rentzy.entity.ReviewEntity;
import com.rentzy.repository.ReviewRepository;
import com.rentzy.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewConverter reviewConverter;

    @Override
    public Page<ReviewDTO> getAllReviews(Pageable pageable) {
        Page<ReviewEntity> page = reviewRepository.findAll(pageable);
        return page.map(reviewConverter::toDTO);
    }

    @Override
    public ReviewDTO getReviewById(String id) {
        ReviewEntity reviewEntity = reviewRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));
        return reviewConverter.toDTO(reviewEntity);
    }

    @Override
    public ReviewDTO createReview(ReviewDTO reviewDTO) {
        ReviewEntity reviewEntity = reviewConverter.toEntity(reviewDTO);
        ReviewEntity savedEntity = reviewRepository.save(reviewEntity);
        return reviewConverter.toDTO(savedEntity);
    }

    @Override
    public ReviewDTO updateReview(String id, ReviewDTO reviewDTO) {
        ReviewEntity existingReview = reviewRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));
        reviewConverter.updateReviewFromDto(reviewDTO, existingReview);
        ReviewEntity savedEntity = reviewRepository.save(existingReview);
        return reviewConverter.toDTO(savedEntity);
    }

    @Override
    public void deleteReview(String id) {
        ReviewEntity reviewEntity = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));
        reviewRepository.delete(reviewEntity);
    }

    @Override
    public Page<ReviewDTO> getReviewByUserId(String userId, Pageable pageable) {
        Page<ReviewEntity> reviewEntity = reviewRepository.findByUserId(userId, pageable);
        if (reviewEntity.isEmpty()) {
            throw new ResourceNotFoundException("No reviews found for user ID: " + userId);
        }
        return reviewEntity.map(reviewConverter::toDTO);
    }

    @Override
    public Page<ReviewDTO> getReviewByPostId(String postId, Pageable pageable) {
        Page<ReviewEntity> reviewEntity = reviewRepository.findByPostId(postId, pageable);
        if (reviewEntity.isEmpty()) {
            throw new ResourceNotFoundException("No reviews found for post ID: " + postId);
        }
        return reviewEntity.map(reviewConverter::toDTO);
    }

    @Override
    public double getAverageRatingByPostId(String postId) {
        return reviewRepository.findByPostId(postId).stream()
                .mapToInt(ReviewEntity::getRating)
                .average()
                .orElse(0.0);
    }
}
