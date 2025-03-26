package com.model.entity;

import com.enums.AppointmentStatus;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "appointment")
public class AppointmentEntity {
    @Id
    private String id;
    private String postId; // Post ID
    private String userId; // User ID (renter)
    private String ownerId; // User ID (owner)
    private Date startDate;
    private Date endDate;
    private AppointmentStatus status; // pending, confirmed, canceled

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;
}
