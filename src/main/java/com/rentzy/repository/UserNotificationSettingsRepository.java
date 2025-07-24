package com.rentzy.repository;

import com.rentzy.entity.UserNotificationSettingsEntity;
import com.rentzy.enums.notification.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserNotificationSettingsRepository extends MongoRepository<UserNotificationSettingsEntity, String> {
    Page<UserNotificationSettingsEntity> findByUserId(String userId, Pageable pageable);

    @Query("{'emailEnabled': true, 'typePreferences.?0': true}")
    Page<UserNotificationSettingsEntity> findUsersWithEmailEnabledForType(NotificationType type, Pageable pageable);
}
