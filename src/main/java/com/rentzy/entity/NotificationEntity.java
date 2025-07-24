package com.rentzy.entity;

import com.rentzy.enums.notification.*;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Map;

@Data
@Document(collection = "notifications")
public class NotificationEntity {
    @Id
    private String id;

    private String userId; // User ID

    private String relatedEntityId; // appointmentId,...
    private String relatedEntityType;

    private String title;
    private String message;

    private NotificationType type; // appointment, review, system
    private NotificationPriority priority; // HIGH, MEDIUM, LOW

    private Map<String, Object> metadata;

    private boolean read;
    private boolean requiresAction;

    private Date createdAt;
    private Date readAt;
    private Date scheduledFor;
}
