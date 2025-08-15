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
    Page<AppointmentResponseDTO> getAppointmentsByPostId(String postId, Pageable pageable);
    Page<AppointmentResponseDTO> getAppointmentsByUserId(String userId, Pageable pageable);
    Page<AppointmentResponseDTO> getAppointmentsByOwnerId(String ownerId, Pageable pageable);
    Page<AppointmentResponseDTO> getAppointmentsByDate(Date date, Pageable pageable);
    AppointmentResponseDTO confirmAppointment(String appointmentId, String ownerId);
    AppointmentResponseDTO cancelAppointment(String appointmentId, String ownerId, String reason);
    AppointmentResponseDTO rejectAppointment(String appointmentId, String ownerId, String reason);
    AppointmentResponseDTO completeAppointment(String appointmentId, String ownerId);
    AppointmentResponseDTO rescheduleAppointment(AppointmentRequestDTO request);
    void sendAppointmentReminder();
}
