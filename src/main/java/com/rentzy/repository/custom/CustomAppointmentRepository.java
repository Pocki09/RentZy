package com.rentzy.repository.custom;

import com.rentzy.model.dto.AppointmentSearchDTO;
import com.rentzy.model.entity.AppointmentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface CustomAppointmentRepository {
    // Tìm lịch hẹn trong khoảng thời gian (từ ngày A đến ngày B)
    List<AppointmentEntity> findByDateBetween(Date startDate, Date endDate);

    boolean isTimeSlotAvailable(String postId, Date startTime, Date endTime);

    Page<AppointmentEntity> searchAppointments(AppointmentSearchDTO searchCriteria, Pageable pageable);
}
