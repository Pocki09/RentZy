package com.rentzy.service;

import com.rentzy.model.dto.ReviewDTO;

import java.util.List;

public interface ReviewService {
    List<ReviewDTO> getAllReviews();
    ReviewDTO getReviewById(String id);
    ReviewDTO createReview(ReviewDTO reviewDTO);
    ReviewDTO updateReview(String id, ReviewDTO reviewDTO);
    void deleteReview(String id);
    List<ReviewDTO> getReviewByUserId(String userId);
    List<ReviewDTO> getReviewByPostId(String postId);
    double getAverageRatingByPostId(String postId);
}
