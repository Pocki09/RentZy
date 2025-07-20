package com.rentzy.repository;

import com.rentzy.entity.AppointmentEntity;
import com.rentzy.enums.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends MongoRepository<AppointmentEntity, String> {
    Page<AppointmentEntity> findAll(Pageable pageable);

    // Tìm lịch hẹn theo ID
    Optional<AppointmentEntity> findById(String id);

    // Tìm lịch hẹn theo bài đăng (postId)
    Page<AppointmentEntity> findByPostId(String postId, Pageable pageable);

    // Tìm lịch hẹn theo người đặt lịch (userId)
    Page<AppointmentEntity> findByUserId(String userId, Pageable pageable);

    // Tìm lịch hẹn theo chủ nhà (ownerId)
    Page<AppointmentEntity> findByOwnerId(String ownerId, Pageable pageable);

    // Tìm lịch hẹn theo ngày (date)
    Page<AppointmentEntity> findByDate(Date date, Pageable pageable);

    // Kiểm tra xem lịch hẹn có tồn tại không
    boolean existsById(String id);

    // Xóa lịch hẹn theo ID
    void deleteById(String id);

    Page<AppointmentEntity> findByStatus(AppointmentStatus status, Pageable pageable);

    Page<AppointmentEntity> findByDateBetween(Date startDate, Date endDate, Pageable pageable);

    @Query(value = "{'status': ?0}", count = true)
    long countAppointmentsByStatus(AppointmentStatus status);

    @Query("{'postId':?0, 'date':?1, 'status':{$in:[?2,?3]}}")
    List<AppointmentEntity> findConflictingAppointments(String postId, Date date, AppointmentStatus confimed, AppointmentStatus pending);
}
