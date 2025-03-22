package com.service;

import com.model.dto.ReviewDTO;
import com.model.dto.UserDTO;

import java.util.List;
import java.util.Optional;

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
