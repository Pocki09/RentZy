package com.rentzy.repository;

import com.rentzy.entity.NotificationEntity;
import com.rentzy.enums.notification.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface NotificationRepository extends MongoRepository<NotificationEntity, String> {
    Page<NotificationEntity> findByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);
    Page<NotificationEntity> findByUserIdAndReadFalseOrderByCreatedAtDesc(String userId, Pageable pageable);

    long countByUserIdAndReadFalse(String userId);

    Page<NotificationEntity> findByUserIdAndType(String userId, NotificationType type, Pageable pageable);
    Page<NotificationEntity> findByRelatedEntityIdAndRelatedEntityType(String relatedEntityId, String relatedEntityType, Pageable pageable);
    Page<NotificationEntity> findByScheduledForBeforeAndReadFalse(Date scheduledFor, Pageable pageable);

    void deleteByCreatedAtBefore(Date createdAt);
    Page<NotificationEntity> findByUserIdAndRequiresActionTrueAndReadFalse(String userId, Pageable pageable);
}
