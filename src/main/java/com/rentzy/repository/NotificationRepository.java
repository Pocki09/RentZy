package com.rentzy.repository;

import com.rentzy.enums.NotificationType;
import com.rentzy.entity.NotificationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends MongoRepository<NotificationEntity, String> {
    // 1. Tìm thông báo theo ID
    Optional<NotificationEntity> findById(String id);

    // 2. Tìm thông báo theo người dùng (userId)
    Page<NotificationEntity> findByUserId(String userId, Pageable pageable);

    List<NotificationEntity> findByUserId(String userId);

    // 3. Tìm thông báo theo loại (type)
    Page<NotificationEntity> findByType(String type, Pageable pageable);

    // 4. Tìm thông báo chưa đọc (isRead = false)
    Page<NotificationEntity> findByIsReadFalse(Pageable pageable);

    // 5. Tìm thông báo theo loại và trạng thái đã đọc
    Page<NotificationEntity> findByTypeAndIsRead(NotificationType type, boolean isRead, Pageable pageable);

    Page<NotificationEntity> findByUserIdAndIsRead(String userId, boolean isRead, Pageable pageable);

    // 6. Kiểm tra xem thông báo có tồn tại không
    boolean existsById(String id);

    // 7. Xóa thông báo theo ID
    void deleteById(String id);
}
