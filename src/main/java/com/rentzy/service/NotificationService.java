package com.rentzy.service;

import com.rentzy.model.dto.request.NotificationRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationService {
    Page<NotificationRequestDTO> getAllNotifications(Pageable pageable);
    void deleteNotification(String id);
    void deleteNotifications(List<String> ids);
    Page<NotificationRequestDTO> getNotificationsByUserId(String userId, Pageable pageable);
    Page<NotificationRequestDTO> getUnreadNotificationsByUserId(String userId, Pageable pageable);
    void markNotificationsAsRead(List<String> ids);
    void markAllNotificationsAsRead(String userId);
}
