package com.rentzy.model.dto.request;

import com.rentzy.enums.PostStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class PostRequestDTO {
    @NotBlank(message = "Property name is required")
    private String propertyName;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Double price;  // Đổi từ minPrice/maxPrice

    @NotNull(message = "Area is required")
    @Positive(message = "Area must be positive")
    private Double area;

    @NotBlank(message = "Address is required")
    private String address;

    private String createdBy;

    private List<String> images;
    private List<String> utilities;
}
