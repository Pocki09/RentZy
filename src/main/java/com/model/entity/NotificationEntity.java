package com.model.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "notifications")
public class NotificationEntity {
    @Id
    private String id;
    private String userId; // User ID
    private String message;
    private String type; // appointment, review, system
    private boolean isRead;
    private Date createdAt;
}
