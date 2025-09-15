package com.rentzy.model.dto.request;


import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class ReviewRequestDTO {
    @NotBlank(message = "Post ID is required")
    private String postId;

    @NotNull(message = "Overall rating is required")
    @Min(value = 1, message = "Rating must be between 1 and 5")
    @Max(value = 5, message = "Rating must be between 1 and 5")
    private Integer overallRating;

    @Size(max = 1000, message = "Comment cannot exceed 1000 characters")
    private String comment;

    private List<String> reviewImages;

    private List<String> tags;

    private Instant rentedFrom;
    private Instant rentedTo;
}
