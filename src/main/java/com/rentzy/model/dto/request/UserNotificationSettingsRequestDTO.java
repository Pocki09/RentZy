package com.rentzy.model.dto.request;

import lombok.Data;

import java.util.Map;

@Data
public class UserNotificationSettingsRequestDTO {
    private String userId;
    private boolean emailEnabled;
    private boolean smsEnabled;
    private boolean pushEnabled;
    private boolean inAppEnabled;
    private Map<String, Boolean> typePreferences;
    private String timezone;
    private Integer quietHoursStart;
    private Integer quietHoursEnd;
}
