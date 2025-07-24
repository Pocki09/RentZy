package com.rentzy.entity;

import com.rentzy.enums.notification.NotificationChanel;
import com.rentzy.enums.notification.NotificationRecipient;
import com.rentzy.enums.notification.NotificationStatus;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "notification_deliveries")
public class NotificationDeliveryEntity {
    @Id
    private String id;

    private String notificationId;
    private String userId;

    private NotificationStatus status; //PENDING,...
    private NotificationChanel chanel; // EMAIL, SMS,... gửi
    private NotificationRecipient recipient; //EMAIL, PHONE,.. nhận

    private String errorMessage;

    private Date attemptAt;
    private Date deliveredAt;

    private int retryCount;
}
