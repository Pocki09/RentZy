package com.rentzy.repository.custom;

import com.rentzy.model.entity.ReviewEntity;

import java.util.List;

public interface CustomReviewRepository {
    List<ReviewEntity> findByRatingBetween(Integer minRating, Integer maxRating);
}
