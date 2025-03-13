package com.repository;

import com.enums.PostStatus;
import com.model.entity.PostEntity;
import com.repository.custom.CustomPostRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PostRepository extends MongoRepository<PostEntity, String>, CustomPostRepository {

}
