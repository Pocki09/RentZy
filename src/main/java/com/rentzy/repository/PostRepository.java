package com.rentzy.repository;

import com.rentzy.enums.PostStatus;
import com.rentzy.entity.PostEntity;
import com.rentzy.repository.custom.CustomPostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface PostRepository extends MongoRepository<PostEntity, String>, CustomPostRepository {
    Page<PostEntity> findByStatus(PostStatus status, Pageable pageable);

    Page<PostEntity> findByStatusAndPriceBetween(PostStatus status, double minPrice, double maxPrice, Pageable pageable);

    Page<PostEntity> findByStatusAndCreatedBy(PostStatus status, String createdBy, Pageable pageable);

    Page<PostEntity> findByStatusOrderByCreatedAtDesc(PostStatus status, Pageable pageable);

    Page<PostEntity> findByCreatedBy(String createdBy, Pageable pageable);

    long countByStatus(PostStatus status);
}
