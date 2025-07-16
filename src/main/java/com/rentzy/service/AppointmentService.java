package com.rentzy.service;

import com.rentzy.model.dto.response.AppointmentResponseDTO;
import com.rentzy.model.dto.request.AppointmentRequestDTO;

import java.util.Date;
import java.util.Optional;

public interface AppointmentService {
    AppointmentResponseDTO createAppointment(AppointmentRequestDTO request);
    AppointmentResponseDTO updateAppointment(String id, AppointmentRequestDTO request);
    void cancelAppointment(String id);
    Optional<AppointmentResponseDTO> getAppointmentById(String id);
    boolean isTimeSlotAvailable(String postId, Date startTime, Date endTime);
    void sendAppointmentReminder(String appointmentId);
}
