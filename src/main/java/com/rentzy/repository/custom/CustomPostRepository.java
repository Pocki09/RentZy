package com.rentzy.repository.custom;

import com.rentzy.model.dto.request.PostRequestDTO;
import com.rentzy.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomPostRepository {
    // Tìm bài đăng theo nhiều tiêu chí (địa chỉ, giá, tiện ích, trạng thái)
    Page<PostEntity> findByFilters(PostRequestDTO criteria, Pageable pageable);
}
