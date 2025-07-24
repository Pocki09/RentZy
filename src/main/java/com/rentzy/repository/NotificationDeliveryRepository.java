package com.rentzy.repository;

import com.rentzy.entity.NotificationDeliveryEntity;
import com.rentzy.enums.notification.NotificationChanel;
import com.rentzy.enums.notification.NotificationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface NotificationDeliveryRepository extends MongoRepository<NotificationDeliveryEntity, String>{
    Page<NotificationDeliveryEntity> findByNotificationIdOrderByAttemptAtDesc(String notificationId, Pageable pageable);
    Page<NotificationDeliveryEntity> findByUserIdAndChanel(String userId, NotificationChanel chanel, Pageable pageable);
    @Query("{'status': 'FAILED', 'retryCount': {'$lt': ?0}}")
    Page<NotificationDeliveryEntity> findFailedDeliveriesForRetry(int maxRetry, Pageable pageable);
    long countByChanelAndStatusAndAttemptAtBetween(NotificationChanel chanel, NotificationStatus status, Date startAt, Date endAt);
}
