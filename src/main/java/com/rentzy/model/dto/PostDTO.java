package com.rentzy.model.dto;

import com.rentzy.enums.PostStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class PostDTO {
    @NotNull
    private String id;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Price is required")
    private double price;

    @NotNull(message = "Area is required")
    private double area;

    @NotBlank(message = "Address is required")
    private String address;

    private List<String> images; // Danh sách hình ảnh (có thể là optional)
    private Set<String> utilities; // Danh sách tiện ích (có thể là optional)
    private PostStatus status; // Trạng thái bài đăng
}
