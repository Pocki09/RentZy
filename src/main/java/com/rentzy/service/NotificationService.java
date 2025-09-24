package com.rentzy.service;

import com.rentzy.entity.AppointmentEntity;
import com.rentzy.entity.NotificationEntity;
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

    void sendEmailNotification(String recipient, String subject, String content);

    NotificationEntity createNotification(NotificationRequestDTO notificationRequestDTO);
    Page<NotificationResponseDTO> getUserNotifications(NotificationRequestDTO request);

    void markAsRead(String notificationId);
    void markAllAsRead(String userId);
    void bulkMarkAsRead(List<String> notificationIds);

    void deleteNotification(String notificationId);
    void deleteOldNotifications(int daysOld);

    UserNotificationSettingsResponseDTO getUserNotificationSettings(UserNotificationSettingsRequestDTO request);
    void updateUserSettings(UserNotificationSettingsRequestDTO request);

    void processScheduledNotifications();
}
