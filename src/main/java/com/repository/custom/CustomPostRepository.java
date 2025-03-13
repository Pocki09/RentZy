package com.repository.custom;

import com.enums.PostStatus;
import com.model.entity.PostEntity;

import java.util.List;

public interface CustomPostRepository {
    // Tìm bài đăng theo tiêu đề và trạng thái
    List<PostEntity> findByTitleAndStatus(String title, PostStatus status);

    // Tìm bài đăng theo địa chỉ và khoảng giá
    List<PostEntity> findByAddressAndPriceBetween(String address, double minPrice, double maxPrice);

    // Tìm bài đăng theo tiện ích và trạng thái
    List<PostEntity> findByUtilitiesAndStatus(String utility, PostStatus status);
}
