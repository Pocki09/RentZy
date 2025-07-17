package com.rentzy.repository;

import com.rentzy.entity.ReviewEntity;
import com.rentzy.repository.custom.CustomReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ReviewRepository extends MongoRepository<ReviewEntity, String>, CustomReviewRepository {
    // 1. Tìm đánh giá theo ID
    Optional<ReviewEntity> findById(String id);

    // 2. Tìm đánh giá theo bài đăng (postId)
    Page<ReviewEntity> findByPostId(String postId, Pageable pageable);

    Optional<ReviewEntity> findByPostId(String postId);

    // 3. Tìm đánh giá theo người đánh giá (userId)
    Page<ReviewEntity> findByUserId(String userId, Pageable pageable);

    // 4. Kiểm tra xem đánh giá có tồn tại không
    boolean existsById(String id);

    // 5. Xóa đánh giá theo ID
    void deleteById(String id);
}
