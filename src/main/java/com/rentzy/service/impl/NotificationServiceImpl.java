package com.rentzy.service.impl;

import com.rentzy.converter.NotificationConverter;
import com.rentzy.exception.ResourceNotFoundException;
import com.rentzy.model.dto.request.NotificationRequestDTO;
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
    public Page<NotificationRequestDTO> getAllNotifications(Pageable pageable) {
        return null;
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
    public Page<NotificationRequestDTO> getNotificationsByUserId(String userId, Pageable pageable) {
        return null;
    }

    @Override
    public Page<NotificationRequestDTO> getUnreadNotificationsByUserId(String userId , Pageable pageable) {
        return null;
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


    }
}
