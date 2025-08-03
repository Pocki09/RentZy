package com.rentzy.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

@Data
@Document(collection = "user_notification_settings")
public class UserNotificationSettingsEntity {
    @Id
    private String id;

    private String userId;

    private boolean emailEnabled;
    private boolean smsEnabled;
    private boolean pushEnabled;
    private boolean inAppEnabled;

    private Map<String, Boolean> typePreferences; // "APPOINTMENT_CREATED" -> true/false

    private String timezone = "Asia/Ho_Chi_Minh";
    private int quietHoursStart = 22;
    private int quietHoursEnd = 7;

    private Date createdAt;
    private LocalDateTime updatedAt;
}
