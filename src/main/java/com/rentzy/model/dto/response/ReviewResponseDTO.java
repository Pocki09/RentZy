package com.rentzy.model.dto.response;

import java.time.Instant;
import java.util.List;

public class ReviewResponseDTO {
    private String id;
    private String postId;
    private String userId;

    // Ratings
    private Integer overallRating;

    private String comment;
    private List<String> reviewImages;
    private List<String> tags;

    private Instant rentedFrom;
    private Instant rentedTo;

    private boolean verified;
    private Integer helpfulCount;

    private Instant createdAt;
    private Instant updatedAt;
}
