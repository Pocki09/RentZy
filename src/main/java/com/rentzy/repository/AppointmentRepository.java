package com.rentzy.repository;

import com.rentzy.entity.AppointmentEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends MongoRepository<AppointmentEntity, String> {
    // Tìm lịch hẹn theo ID
    Optional<AppointmentEntity> findById(String id);

    // Tìm lịch hẹn theo bài đăng (postId)
    List<AppointmentEntity> findByPostId(String postId);

    // Tìm lịch hẹn theo người đặt lịch (userId)
    List<AppointmentEntity> findByUserId(String userId);

    // Tìm lịch hẹn theo chủ nhà (ownerId)
    List<AppointmentEntity> findByOwnerId(String ownerId);

    // Tìm lịch hẹn theo ngày (date)
    List<AppointmentEntity> findByDate(Date date);

    // Kiểm tra xem lịch hẹn có tồn tại không
    boolean existsById(String id);

    // Xóa lịch hẹn theo ID
    void deleteById(String id);
}
