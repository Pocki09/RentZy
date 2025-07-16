package com.rentzy.model.dto;


import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ReviewDTO {
    @NotNull
    private String id;

    @NotBlank(message = "Post ID is required")
    private String postId;

    @NotBlank(message = "User ID is required")
    private String userId;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;

    @Size(max = 500, message = "Comment must not exceed 500 characters")
    private String comment;
}
