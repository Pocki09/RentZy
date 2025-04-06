package com.rentzy.service;

import com.rentzy.model.dto.NotificationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationService {
    Page<NotificationDTO> getAllNotifications(Pageable pageable);
    void deleteNotification(String id);
    void deleteNotifications(List<String> ids);
    Page<NotificationDTO> getNotificationsByUserId(String userId, Pageable pageable);
    Page<NotificationDTO> getUnreadNotificationsByUserId(String userId, Pageable pageable);
    void markNotificationsAsRead(List<String> ids);
    void markAllNotificationsAsRead(String userId);
}
