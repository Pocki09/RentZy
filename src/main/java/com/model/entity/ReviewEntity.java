package com.model.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "reviews")
public class ReviewEntity {
    @Id
    private String id;
    private String postId; // Post ID
    private String userId; // User ID
    private Integer rating; // 1-5
    private String comment;
    private Date createdAt;
    private Date updatedAt;
}
