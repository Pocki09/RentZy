package com.rentzy.repository;

import com.rentzy.entity.ReviewEntity;
import com.rentzy.repository.custom.CustomReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface ReviewRepository extends MongoRepository<ReviewEntity, String>, CustomReviewRepository {
    Page<ReviewEntity> findByPostIdOrderByCreatedAtDesc(String postId, Pageable pageable);
    // Lấy review đã verified (tin cậy hơn)
    Page<ReviewEntity> findByPostIdAndVerifiedTrueOrderByCreatedAtDesc(String postId, Pageable pageable);
    // Kiểm tra user đã review post này chưa
    boolean existsByPostIdAndUserId(String postId, String userId);
    // Lấy review của user (xem lịch sử review của người đó)
    Page<ReviewEntity> findByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);
    // Tính rating trung bình của 1 post
    @Query("{ 'postId': ?0 }")
    Optional<ReviewEntity> findByPostId(String postId);
    // Lấy review theo rating (SV muốn xem review tốt/xấu)
    Page<ReviewEntity> findByPostIdAndOverallRatingGreaterThanEqualOrderByCreatedAtDesc(
            String postId, Integer minRating, Pageable pageable);
    // Lấy review có tag cụ thể
    Page<ReviewEntity> findByPostIdAndTagsContaining(String postId, String tag, Pageable pageable);
    // Đếm số review của post
    long countByPostId(String postId);
    // Lấy review cần xác thực (cho admin)
    Page<ReviewEntity> findByVerifiedFalseOrderByCreatedAtDesc(Pageable pageable);
    // Lấy review bị báo cáo nhiều
    @Query("{ 'reportedBy': { $exists: true, $ne: [] }, " +
            "  \"$expr\": { \"$gte\": [ { \"$size\": \"$reportedBy\" }, ?0 ] } }")
    Page<ReviewEntity> findReviewsWithMinReports(int minReports, Pageable pageable);
}
