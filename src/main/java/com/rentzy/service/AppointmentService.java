package com.rentzy.service;

import com.rentzy.model.dto.response.AppointmentResponseDTO;
import com.rentzy.model.dto.request.AppointmentRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.Optional;

public interface AppointmentService {
    AppointmentResponseDTO createAppointment(AppointmentRequestDTO request);
    Optional<AppointmentResponseDTO> getAppointmentById(String id);
    AppointmentResponseDTO updateAppointment(String id, AppointmentRequestDTO request);
    Page<AppointmentResponseDTO> getAppointmentsByPostId(String postId, Pageable pageable);
    Page<AppointmentResponseDTO> getAppointmentsByUserId(String userId, Pageable pageable);
    Page<AppointmentResponseDTO> getAppointmentsByOwnerId(String ownerId, Pageable pageable);
    Page<AppointmentResponseDTO> getAppointmentsByDate(Date date, Pageable pageable);
    void cancelAppointment(String id);
    void rejectAppointment(String id);
    void completeAppointment(String id);
    AppointmentResponseDTO rescheduleAppointment(String id, AppointmentRequestDTO request);
    boolean isTimeSlotAvailable(String postId, Date startTime, Date endTime);
    void sendAppointmentReminder(String appointmentId);
}
