package com.rentzy.service;

import com.rentzy.entity.AppointmentEntity;
import com.rentzy.entity.NotificationEntity;
import com.rentzy.enums.notification.NotificationRecipient;
import com.rentzy.enums.notification.NotificationType;
import com.rentzy.model.dto.request.NotificationRequestDTO;
import com.rentzy.model.dto.request.UserNotificationSettingsRequestDTO;
import com.rentzy.model.dto.response.NotificationResponseDTO;
import com.rentzy.model.dto.response.UserNotificationSettingsResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationService {

    void sendAppointmentNotification(AppointmentEntity appointment, NotificationType type);
    void sendAppointmentReminder(AppointmentEntity appointment);

    void sendEmailNotification(NotificationRecipient recipient, String subject, String content);
    void sendSMSNotification(String phongNumber, String content);
    void sendPushNotification(String userId, String title, String message);

    NotificationEntity createNotification(NotificationRequestDTO notificationRequestDTO);
    Page<NotificationResponseDTO> getUserNotifications(String userId, Pageable pageable);

    void markAsRead(String notificationId);
    void markAllAsRead(String userId);
    void bulkMarkAsRead(List<String> notificationIds);

    void deleteNotification(String notificationId);
    void deleteOldNotifications(int daysOld);

    UserNotificationSettingsResponseDTO getUserNotificationSettings(String userId);
    void updateUserSettings(String userId, UserNotificationSettingsRequestDTO request);

    void processScheduledNotifications();
    void retryFailedDeliveries();
}
