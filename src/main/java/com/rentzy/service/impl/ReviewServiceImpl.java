package com.rentzy.service.impl;

import com.rentzy.converter.ReviewConverter;
import com.rentzy.entity.ReviewEntity;
import com.rentzy.model.dto.response.ReviewResponseDTO;
import com.rentzy.repository.ReviewRepository;
import com.rentzy.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewConverter reviewConverter;

    @Override
    public Page<ReviewEntity> getReviewsByPost(String postId, Pageable pageable) {
        return reviewRepository.findByUserIdOrderByCreatedAtDesc(postId, pageable);
    }

    @Override
    public Page<ReviewEntity> getVerifiedReviewsByPost(String postId, Pageable pageable) {
        return reviewRepository.findByPostIdAndVerifiedTrueOrderByCreatedAtDesc(postId, pageable);
    }

    @Override
    public Page<ReviewEntity> getReviewsByMinRating(String postId, Integer minRating, Pageable pageable) {
        return reviewRepository.findByPostIdAndOverallRatingGreaterThanEqualOrderByCreatedAtDesc(postId, minRating, pageable);
    }

    @Override
    public Page<ReviewEntity> getReviewsByTag(String postId, String tag, Pageable pageable) {
        return reviewRepository.findByPostIdAndTagsContaining(postId, tag, pageable);
    }

    @Override
    @Transactional
    public ReviewResponseDTO createReview(ReviewEntity review, String userId) {
        if (reviewRepository.existsByPostIdAndUserId(review.getPostId(), userId)) {
            throw new RuntimeException("You have already reviewed this post");
        }

        review.setUserId(userId);
        review.setVerified(false); // Mặc định chưa xác thực
        review.setReportedBy(new ArrayList<>());

        reviewRepository.save(review);
        return reviewConverter.toDTO(review);
    }

    @Override
    public ReviewResponseDTO updateReview(String reviewId, ReviewEntity updatedReview, String userId) {
        Optional<ReviewEntity> existingOpt = reviewRepository.findById(reviewId);

        if (existingOpt.isEmpty()) {
            throw new RuntimeException("Review not found");
        }

        ReviewEntity existing = existingOpt.get();
        if (!existing.getUserId().equals(userId)) {
            throw new RuntimeException("You can only edit your own review");
        }

        existing.setOverallRating(updatedReview.getOverallRating());
        existing.setComment(updatedReview.getComment());
        existing.setReviewImages(updatedReview.getReviewImages());
        existing.setTags(updatedReview.getTags());
        existing.setRentedFrom(updatedReview.getRentedFrom());
        existing.setRentedTo(updatedReview.getRentedTo());

        // Reset verified status when edited
        existing.setVerified(false);

        reviewRepository.save(existing);

        return reviewConverter.toDTO(existing);
    }

    @Override
    public void deleteReview(String reviewId, String userId) {
        Optional<ReviewEntity> existingOpt = reviewRepository.findById(reviewId);

        if (existingOpt.isEmpty()) {
            throw new RuntimeException("Review not found");
        }

        ReviewEntity existing = existingOpt.get();

        if (!existing.getUserId().equals(userId)) {
            throw new RuntimeException("You can only delete your own review");
        }

        reviewRepository.deleteById(reviewId);
    }

    @Override
    public ReviewResponseDTO markHelpful(String reviewId) {
        Optional<ReviewEntity> reviewOpt = reviewRepository.findById(reviewId);

        if (reviewOpt.isEmpty()) {
            throw new RuntimeException("Review not found");
        }

        ReviewEntity review = reviewOpt.get();
        review.setHelpfulCount(review.getHelpfulCount() + 1);

        reviewRepository.save(review);

        return reviewConverter.toDTO(review);
    }

    @Override
    public ReviewResponseDTO reportReview(String reviewId, String reporterId) {
        Optional<ReviewEntity> reviewOpt = reviewRepository.findById(reviewId);

        if (reviewOpt.isEmpty()) {
            throw new RuntimeException("Review not found");
        }

        ReviewEntity review = reviewOpt.get();

        if (review.getReportedBy() == null) {
            review.setReportedBy(new ArrayList<>());
        }

        // Không cho báo cáo duplicate
        if (!review.getReportedBy().contains(reporterId)) {
            review.getReportedBy().add(reporterId);
        }

        reviewRepository.save(review);

        return reviewConverter.toDTO(review);
    }

    @Override
    public ReviewResponseDTO verifyReview(String reviewId) {
        Optional<ReviewEntity> reviewOpt = reviewRepository.findById(reviewId);

        if (reviewOpt.isEmpty()) {
            throw new RuntimeException("Review not found");
        }

        ReviewEntity review = reviewOpt.get();
        review.setVerified(true);

        reviewRepository.save(review);

        return reviewConverter.toDTO(review);
    }

    @Override
    public Page<ReviewEntity> getUnverifiedReviews(Pageable pageable) {
        return reviewRepository.findByVerifiedFalseOrderByCreatedAtDesc(pageable);
    }

    @Override
    public Page<ReviewEntity> getReportedReviews(int minReports, Pageable pageable) {
        return reviewRepository.findReviewsWithMinReports(minReports, pageable);
    }

    @Override
    public long countReviewsByPost(String postId) {
        return reviewRepository.countByPostId(postId);
    }
}
