package com.repository.custom;

import com.model.dto.PostSearchDTO;
import com.model.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomPostRepository {
    // Tìm bài đăng theo nhiều tiêu chí (địa chỉ, giá, tiện ích, trạng thái)
    Page<PostEntity> findByFilters(PostSearchDTO criteria, Pageable pageable);
}
