package com.rentzy.repository;

import com.rentzy.enums.PostStatus;
import com.rentzy.model.entity.PostEntity;
import com.rentzy.repository.custom.CustomPostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface PostRepository extends MongoRepository<PostEntity, String>, CustomPostRepository {
    Page<PostEntity> findByStatus(PostStatus status, Pageable pageable);
}
