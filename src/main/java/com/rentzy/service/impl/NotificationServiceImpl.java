package com.rentzy.service.impl;

import com.rentzy.entity.AppointmentEntity;
import com.rentzy.enums.notification.NotificationRecipient;
import com.rentzy.enums.notification.NotificationType;
import com.rentzy.model.dto.request.NotificationRequestDTO;
import com.rentzy.model.dto.request.UserNotificationSettingsRequestDTO;
import com.rentzy.model.dto.response.NotificationResponseDTO;
import com.rentzy.model.dto.response.UserNotificationSettingsResponseDTO;
import com.rentzy.repository.NotificationDeliveryRepository;
import com.rentzy.repository.NotificationRepository;
import com.rentzy.repository.UserNotificationSettingsRepository;
import com.rentzy.service.NotificationService;
import com.rentzy.service.UserService;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {

    private NotificationRepository notificationRepository;
    private NotificationDeliveryRepository notificationDeliveryRepository;
    private UserNotificationSettingsRepository userNotificationSettingsRepository;
    private JavaMailSender javaMailSender;
    private UserService userService;

    @Override
    public void sendAppointmentNotification(AppointmentEntity appointment, NotificationType type) {

    }

    @Override
    public void sendAppointmentReminder(AppointmentEntity appointment) {

    }

    @Override
    public void sendEmailNotification(NotificationRecipient recipient, String subject, String content) {

    }

    @Override
    public void sendSMSNotification(String phongNumber, String content) {

    }

    @Override
    public void sendPushNotification(String userId, String title, String message) {

    }

    @Override
    public NotificationResponseDTO createNotification(NotificationRequestDTO notificationRequestDTO) {
        return null;
    }

    @Override
    public Page<NotificationResponseDTO> getUserNotifications(String userId, Pageable pageable) {
        return null;
    }

    @Override
    public void markAsRead(String notificationId) {

    }

    @Override
    public void markAllAsRead(String userId) {

    }

    @Override
    public void bulkMarkAsRead(List<String> notificationIds) {

    }

    @Override
    public void deleteNotification(String notificationId) {

    }

    @Override
    public void deleteOldNotifications(int daysOld) {

    }

    @Override
    public UserNotificationSettingsResponseDTO getUserNotificationSettings(String userId) {
        return null;
    }

    @Override
    public void updateUserSettings(String userId, UserNotificationSettingsRequestDTO request) {

    }

    @Override
    public void processScheduledNotifications() {

    }

    @Override
    public void retryFailedDeliveries() {

    }

    private Map<String, Object> buildAppointmentData(AppointmentEntity appointment) {
        Map<String, Object> data = new HashMap<>();
        data.put("appointmentId", appointment.getId());
        data.put("appointmentTime", appointment.getAppointmentDate());
        data.put("status", appointment.getStatus());
        data.put("duration", appointment.getDurationMinutes() + " minutes");
        return data;
    }
}
