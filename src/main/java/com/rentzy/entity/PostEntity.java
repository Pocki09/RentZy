package com.rentzy.entity;

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
    private String propertyName;
    private String title;
    private String description;
    private double price;
    private List<String> images;
    private List<String> utilities;
    private String createdBy; // User ID
    private PostStatus status; // pending, approved, rejected
    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;

    private String ward;
    private String district;
    private String city;

    private String nearUniversities;
    private String transportInfo;
    private String roomType;
    private boolean includeBills;

    private boolean securityCamera;
}
