package com.rentzy.service.impl;

import com.rentzy.converter.NotificationConverter;
import com.rentzy.exception.ResourceNotFoundException;
import com.rentzy.model.dto.NotificationDTO;
import com.rentzy.entity.NotificationEntity;
import com.rentzy.repository.NotificationRepository;
import com.rentzy.service.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;


@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationConverter notificationConverter;

    public NotificationServiceImpl(NotificationRepository repo,
                                   NotificationConverter conv) {
        this.notificationRepository = repo;
        this.notificationConverter  = conv;
    }


    @Override
    public Page<NotificationDTO> getAllNotifications(Pageable pageable) {
        return notificationRepository.findAll(pageable).map(notificationConverter::toDTO);
    }

    @Override
    @Transactional
    public void deleteNotification(String id) {
        if (!notificationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Notification not found with id: " + id);
        }
        notificationRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteNotifications(List<String> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new IllegalArgumentException("List of IDs must not be empty.");
        }
        ids.forEach(id -> {
            if (!notificationRepository.existsById(id)) {
                throw new ResourceNotFoundException("Notification not found with id: " + id);
            }
        });
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
        if (CollectionUtils.isEmpty(ids)) {
            throw new IllegalArgumentException("List of IDs must not be empty.");
        }
        List<NotificationEntity> notifications = notificationRepository.findAllById(ids);
        if (notifications.isEmpty()) {
            throw new ResourceNotFoundException("No notifications found for the given IDs.");
        }
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
