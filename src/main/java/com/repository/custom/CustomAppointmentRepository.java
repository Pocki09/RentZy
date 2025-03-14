package com.repository.custom;

import com.model.entity.AppointmentEntity;

import java.util.Date;
import java.util.List;

public interface CustomAppointmentRepository {
    // Tìm lịch hẹn trong khoảng thời gian (từ ngày A đến ngày B)
    List<AppointmentEntity> findByDateBetween(Date startDate, Date endDate);
}
