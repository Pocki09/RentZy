package com.repository.custom;

import com.model.entity.ReviewEntity;

import java.util.List;

public interface CustomReviewRepository {
    List<ReviewEntity> findByRatingBetween(Integer minRating, Integer maxRating);
}
