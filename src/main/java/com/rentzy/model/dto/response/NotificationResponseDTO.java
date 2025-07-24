package com.rentzy.model.dto.response;

import com.rentzy.enums.notification.NotificationPriority;
import com.rentzy.enums.notification.NotificationType;

import java.util.Date;
import java.util.Map;

public class NotificationResponseDTO {
    private String id;
    private String userId;
    private String relatedEntityId;
    private String relatedEntityType;
    private String title;
    private String message;
    private NotificationType type;
    private NotificationPriority priority;
    private Map<String, Object> metadata;
    private boolean isRead;
    private boolean requiresAction;
    private Date createdAt;
    private Date readAt;
    private Date updatedAt;
}
