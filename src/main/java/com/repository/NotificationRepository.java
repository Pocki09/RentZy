package com.repository;

import com.model.entity.NotificationEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends MongoRepository<NotificationEntity, String> {
    // 1. Tìm thông báo theo ID
    Optional<NotificationEntity> findById(String id);

    // 2. Tìm thông báo theo người dùng (userId)
    List<NotificationEntity> findByUserId(String userId);

    // 3. Tìm thông báo theo loại (type)
    List<NotificationEntity> findByType(String type);

    // 4. Tìm thông báo chưa đọc (isRead = false)
    List<NotificationEntity> findByIsReadFalse();

    // 5. Tìm thông báo theo loại và trạng thái đã đọc
    List<NotificationEntity> findByTypeAndIsRead(String type, boolean isRead);

    // 6. Kiểm tra xem thông báo có tồn tại không
    boolean existsById(String id);

    // 7. Xóa thông báo theo ID
    void deleteById(String id);
}
