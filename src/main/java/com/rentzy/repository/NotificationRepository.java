package com.rentzy.repository;

import com.rentzy.entity.NotificationEntity;
import com.rentzy.enums.notification.NotificationType;
import com.rentzy.model.dto.request.NotificationRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<NotificationEntity, String> {
    List<NotificationEntity> findByUserIdOrderByCreatedAtDesc(String userId);
    List<NotificationEntity> findByUserIdAndReadFalseOrderByCreatedAtDesc(String userId);
    List<NotificationEntity> findByScheduledForBeforeAndReadFalse(Date scheduledFor);
    List<NotificationEntity> findByCreatedAtBefore(Date createdAt);
}
