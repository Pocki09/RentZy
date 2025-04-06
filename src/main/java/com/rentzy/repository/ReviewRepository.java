package com.rentzy.repository;

import com.rentzy.model.entity.ReviewEntity;
import com.rentzy.repository.custom.CustomReviewRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends MongoRepository<ReviewEntity, String>, CustomReviewRepository {
    // 1. Tìm đánh giá theo ID
    Optional<ReviewEntity> findById(String id);

    // 2. Tìm đánh giá theo bài đăng (postId)
    List<ReviewEntity> findByPostId(String postId);

    // 3. Tìm đánh giá theo người đánh giá (userId)
    List<ReviewEntity> findByUserId(String userId);

    // 4. Kiểm tra xem đánh giá có tồn tại không
    boolean existsById(String id);

    // 5. Xóa đánh giá theo ID
    void deleteById(String id);
}
