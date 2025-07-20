package com.rentzy.entity;

import com.rentzy.enums.AppointmentStatus;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "appointment")
public class AppointmentEntity {
    @Id
    private String id;
    private String postId; // Post ID
    private String userId; // User ID (renter)
    private String ownerId;
    private Date appointmentDate;
    private AppointmentStatus status; // pending, confirmed, canceled
    @CreatedDate
    private Date createdAt;
    @LastModifiedDate
    private Date updatedAt;
    private String notes;
    private String cancellationReason;
    private Integer durationMinutes;
    private Boolean reminderSent = false;
    private String contactPhone;
    private Date cancelledAt;
    private Date confirmedAt;
    private Date completedAt;
}
