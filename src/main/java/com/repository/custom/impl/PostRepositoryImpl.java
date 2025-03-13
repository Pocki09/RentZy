package com.repository.custom.impl;

import com.enums.PostStatus;
import com.model.entity.PostEntity;
import com.repository.custom.CustomPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.regex.Pattern;

@Repository
public class PostRepositoryImpl implements CustomPostRepository {
    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public List<PostEntity> findByTitleAndStatus(String title, PostStatus status) {
        Query query = new Query();
        if (title != null && !title.isBlank()) {
            query.addCriteria(Criteria.where("title").regex(Pattern.quote(title), "i"));
        }
        query.addCriteria(Criteria.where("status").is(status));

        return mongoTemplate.find(query, PostEntity.class);
    }

    @Override
    public List<PostEntity> findByAddressAndPriceBetween(String address, double minPrice, double maxPrice) {
        Query query = new Query();
        Criteria criteria = new Criteria();

        if (address != null && !address.isEmpty()) {
            criteria = criteria.and("address").regex(".*" + Pattern.quote(address) + ".*", "i");
        }

        if (minPrice > maxPrice) {
            throw new IllegalArgumentException("minPrice phải nhỏ hơn maxPrice");
        }

        criteria = criteria.and("price").gte(minPrice).lte(maxPrice);
        query.addCriteria(criteria);

        return mongoTemplate.find(query, PostEntity.class);
    }

    @Override
    public List<PostEntity> findByUtilitiesAndStatus(String utility, PostStatus status) {
        Query query = new Query();
        query.addCriteria(Criteria.where("utilities").in(utility)
                .and("status").is(status));
        return mongoTemplate.find(query, PostEntity.class);
    }
}
