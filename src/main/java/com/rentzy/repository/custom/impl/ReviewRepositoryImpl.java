package com.rentzy.repository.custom.impl;

import com.rentzy.entity.ReviewEntity;
import com.rentzy.repository.custom.CustomReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReviewRepositoryImpl implements CustomReviewRepository {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<ReviewEntity> findByRatingBetween(Integer minRating, Integer maxRating) {
        Query query = new Query();
        Criteria criteria = new Criteria();

        if (minRating != null && maxRating != null) {
            if (minRating > maxRating) {
                throw new IllegalArgumentException("minRating phải nhỏ hơn hoặc bằng maxRating");
            }
            // Nếu cả min và max đều có giá trị
            criteria = Criteria.where("rating").gte(minRating).lte(maxRating);
        } else if (minRating != null) {
            // Nếu chỉ có minRating, lấy tất cả lớn hơn hoặc bằng minRating
            criteria = Criteria.where("rating").gte(minRating);
        } else if (maxRating != null) {
            // Nếu chỉ có maxRating, lấy tất cả nhỏ hơn hoặc bằng maxRating
            criteria = Criteria.where("rating").lte(maxRating);
        } else {
            // Nếu cả hai null, không thêm điều kiện (lấy tất cả)
            return mongoTemplate.findAll(ReviewEntity.class);
        }

        query.addCriteria(criteria);
        return mongoTemplate.find(query, ReviewEntity.class);
    }
}
