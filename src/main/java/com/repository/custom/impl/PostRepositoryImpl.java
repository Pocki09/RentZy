package com.repository.custom.impl;

import com.model.dto.PostSearchCriteria;
import com.model.entity.PostEntity;
import com.repository.custom.CustomPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Repository
public class PostRepositoryImpl implements CustomPostRepository {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Page<PostEntity> findByFilters(PostSearchCriteria criteria, Pageable pageable) {
        Query query = new Query();
        List<Criteria> criteriaList = new ArrayList<>();

        // Tìm theo tiêu đề (dùng regex để hỗ trợ tìm kiếm không phân biệt hoa thường)
        if (criteria.getTitle() != null && !criteria.getTitle().isBlank()) {
            criteriaList.add(Criteria.where("title").regex(".*" + Pattern.quote(criteria.getTitle()) + ".*", "i"));
        }

        // Tìm theo địa chỉ
        if (criteria.getAddress() != null && !criteria.getAddress().isBlank()) {
            criteriaList.add(Criteria.where("address").regex(".*" + Pattern.quote(criteria.getAddress()) + ".*", "i"));
        }

        // Tìm theo khoảng giá
        if (criteria.getMinPrice() != null || criteria.getMaxPrice() != null) {
            if (criteria.getMinPrice() != null && criteria.getMaxPrice() != null && criteria.getMinPrice() > criteria.getMaxPrice()) {
                throw new IllegalArgumentException("Min price cannot be greater than Max price");
            }

            Criteria priceCriteria = Criteria.where("price");
            if (criteria.getMinPrice() != null) {
                priceCriteria = priceCriteria.gte(criteria.getMinPrice());
            }
            if (criteria.getMaxPrice() != null) {
                priceCriteria = priceCriteria.lte(criteria.getMaxPrice());
            }
            criteriaList.add(priceCriteria);
        }

        // Tìm theo danh sách tiện ích (chỉ cần 1 tiện ích khớp)
        if (criteria.getUtilities() != null && !criteria.getUtilities().isEmpty()) {
            criteriaList.add(Criteria.where("utilities").in(criteria.getUtilities()));
        }

        // Tìm theo trạng thái bài đăng
        if (criteria.getStatus() != null) {
            criteriaList.add(Criteria.where("status").is(criteria.getStatus()));
        }

        // Nếu có bất kỳ điều kiện nào, thêm vào query
        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        // Nếu pageable bị null, dùng Pageable.unpaged()
        if (pageable == null) {
            pageable = Pageable.unpaged();
        }

        if (pageable.getSort().isSorted()) {
            query.with(pageable.getSort());
        }

        // Lấy tổng số lượng kết quả trước khi phân trang
        long total = mongoTemplate.count(query, PostEntity.class);

        // Áp dụng phân trang
        query.with(pageable);

        // Lấy danh sách bài đăng sau khi áp dụng các điều kiện tìm kiếm
        List<PostEntity> posts = mongoTemplate.find(query, PostEntity.class);

        return new PageImpl<>(posts, pageable, total);
    }
}
