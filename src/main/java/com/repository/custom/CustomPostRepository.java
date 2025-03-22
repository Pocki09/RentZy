package com.repository.custom;

import com.enums.PostStatus;
import com.model.dto.PostSearchCriteria;
import com.model.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomPostRepository {
    // Tìm bài đăng theo nhiều tiêu chí (địa chỉ, giá, tiện ích, trạng thái)
    Page<PostEntity> findByFilters(PostSearchCriteria criteria, Pageable pageable);
}
