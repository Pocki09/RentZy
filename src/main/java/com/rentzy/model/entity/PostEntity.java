package com.rentzy.model.entity;

import com.rentzy.enums.PostStatus;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@Document(collection = "post")
public class PostEntity {
    @Id
    private String id;
    private String title;
    private String description;

    @Indexed
    private double price;
    private double area;

    @Indexed
    private String address;
    private List<String> images;
    private List<String> utilities;
    private String createdBy; // User ID
    private PostStatus status; // pending, approved, rejected

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
