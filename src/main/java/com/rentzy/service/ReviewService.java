package com.rentzy.service;

import com.rentzy.entity.ReviewEntity;
import com.rentzy.model.dto.response.ReviewResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewService {
    Page<ReviewEntity> getReviewsByPost(String postId, Pageable pageable);
    Page<ReviewEntity> getVerifiedReviewsByPost(String postId, Pageable pageable);
    // Lọc review theo rating tối thiểu
    Page<ReviewEntity> getReviewsByMinRating(String postId, Integer minRating, Pageable pageable);
    Page<ReviewEntity> getReviewsByTag(String postId, String tag, Pageable pageable);
    ReviewResponseDTO createReview(ReviewEntity review, String userId);
    ReviewResponseDTO updateReview(String reviewId, ReviewEntity updatedReview, String userId);
    void deleteReview(String reviewId, String userId);
    ReviewResponseDTO markHelpful(String reviewId);
    ReviewResponseDTO reportReview(String reviewId, String reporterId);
    ReviewResponseDTO verifyReview(String reviewId);
    Page<ReviewEntity> getUnverifiedReviews(Pageable pageable);
    // Admin xem review bị báo cáo nhiều
    Page<ReviewEntity> getReportedReviews(int minReports, Pageable pageable);
    long countReviewsByPost(String postId);
}
