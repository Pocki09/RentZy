package com.rentzy.entity;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Data
@Document(collection = "reviews")
public class ReviewEntity {
    @Id
    private String id;
    private String postId; // Post ID
    private String userId; // User ID

    @NotNull
    @Min(1)
    @Max(5)
    private Integer overallRating; // 1-5

    private String comment;
    private List<String> reviewImages;
    private List<String> tags;

    private Instant rentedFrom; // Bắt đầu thuê từ khi nào
    private Instant rentedTo;   // Thuê đến khi nào (null nếu đang thuê)

    private boolean verified; // Admin xác thực người này đã thuê thật

    private Integer helpfulCount;
    private List<String> reportedBy; // Danh sách user báo cáo spam/fake

    @CreatedDate
    private Date createdAt;
    @LastModifiedDate
    private Date updatedAt;
}
