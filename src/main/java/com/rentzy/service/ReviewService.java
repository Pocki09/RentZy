package com.rentzy.service;

import com.rentzy.model.dto.ReviewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewService {
    Page<ReviewDTO> getAllReviews(Pageable pageable);
    ReviewDTO getReviewById(String id);
    ReviewDTO createReview(ReviewDTO reviewDTO);
    ReviewDTO updateReview(String id, ReviewDTO reviewDTO);
    void deleteReview(String id);
    Page<ReviewDTO> getReviewByUserId(String userId, Pageable pageable);
    Page<ReviewDTO> getReviewByPostId(String postId, Pageable pageable);
    double getAverageRatingByPostId(String postId);
}
