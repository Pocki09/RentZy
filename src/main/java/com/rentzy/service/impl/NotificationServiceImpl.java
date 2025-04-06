package com.rentzy.service.impl;

import com.rentzy.converter.NotificationConverter;
import com.rentzy.exception.ResourceNotFoundException;
import com.rentzy.model.dto.NotificationDTO;
import com.rentzy.model.entity.NotificationEntity;
import com.rentzy.repository.NotificationRepository;
import com.rentzy.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationConverter notificationConverter;

    @Override
    public Page<NotificationDTO> getAllNotifications(Pageable pageable) {
        return notificationRepository.findAll(pageable).map(notificationConverter::toDTO);
    }

    @Override
    public void deleteNotification(String id) {
        if (!notificationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Notification not found with id: " + id);
        }
        notificationRepository.deleteById(id);
    }

    @Override
    public void deleteNotifications(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("List of IDs must not be empty.");
        }
        notificationRepository.deleteAllById(ids);
    }

    @Override
    public Page<NotificationDTO> getNotificationsByUserId(String userId, Pageable pageable) {
        return notificationRepository.findByUserId(userId, pageable)
                .map(notificationConverter::toDTO);
    }

    @Override
    public Page<NotificationDTO> getUnreadNotificationsByUserId(String userId , Pageable pageable) {
        return notificationRepository.findByUserIdAndIsRead(userId, false, pageable)
                .map(notificationConverter::toDTO);
    }

    @Override
    public void markNotificationsAsRead(List<String> ids) {
        List<NotificationEntity> notifications = notificationRepository.findAllById(ids);

        if (notifications.isEmpty()) {
            throw new ResourceNotFoundException("No notifications found for the given IDs.");
        }
        // Đánh dấu tất cả là đã đọc
        notifications.forEach(notification -> notification.setRead(true));
        notificationRepository.saveAll(notifications);
    }

    @Override
    public void markAllNotificationsAsRead(String userId) {
        List<NotificationEntity> notifications = notificationRepository.findByUserId(userId);

        if (notifications.isEmpty()) {
            throw new ResourceNotFoundException("No notifications found for the given IDs.");
        }
        // Đánh dấu tất cả là đã đọc
        notifications.forEach(notification -> notification.setRead(true));
        notificationRepository.saveAll(notifications);
    }
}
