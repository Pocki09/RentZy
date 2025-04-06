package com.rentzy.service;

import com.rentzy.model.dto.AppointmentDTO;
import com.rentzy.model.dto.AppointmentSearchDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.Optional;

public interface AppointmentService {
    AppointmentDTO createAppointment(AppointmentDTO appointmentDTO);
    AppointmentDTO updateAppointment(String id, AppointmentDTO appointmentDTO);
    void cancelAppointment(String id);
    Optional<AppointmentDTO> getAppointmentById(String id);

    Page<AppointmentDTO> searchAppointments(AppointmentSearchDTO searchCriteria, Pageable pageable);
    boolean isTimeSlotAvailable(String postId, Date startTime, Date endTime);
    void sendAppointmentReminder(String appointmentId);
}
