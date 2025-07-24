package com.rentzy.model.dto.response;

import lombok.Data;
import java.util.Date;

@Data
public class NotificationDeliveryResponseDTO {
    private String id;
    private String notificationId;
    private String userId;
    private String channel; // EMAIL, SMS, PUSH, IN_APP
    private String status; // PENDING, SENT, DELIVERED, FAILED
    private String recipient; // email, phone, device_token
    private String errorMessage;
    private Date attemptedAt;
    private Date deliveredAt;
    private int retryCount;
} 