package com.rentzy.service.impl;

import com.rentzy.converter.AppointmentConverter;
import com.rentzy.entity.AppointmentEntity;
import com.rentzy.enums.AppointmentStatus;
import com.rentzy.model.dto.response.AppointmentResponseDTO;
import com.rentzy.model.dto.request.AppointmentRequestDTO;
import com.rentzy.repository.AppointmentRepository;
import com.rentzy.service.AppointmentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private AppointmentRepository appointmentRepository;
    private AppointmentConverter appointmentConverter;

    @Override
    @Transactional
    public AppointmentResponseDTO createAppointment(AppointmentRequestDTO request) {
        return null;
    }

    @Override
    @Transactional
    public AppointmentResponseDTO updateAppointment(String id, AppointmentRequestDTO request) {
        Optional<AppointmentEntity> appointmentEntity = appointmentRepository.findById(id);
        if(appointmentEntity.isEmpty()){
            throw new IllegalArgumentException("Không tìm thấy lịch hẹn với ID: " + id);
        }

        AppointmentEntity existingAppointment = appointmentEntity.get();

        appointmentRepository.save(existingAppointment);
        return appointmentConverter.toDTO(existingAppointment);
    }

    @Override
    @Transactional
    public void cancelAppointment(String id) {
        Optional<AppointmentEntity> existingAppointmentOpt = appointmentRepository.findById(id);
        if (existingAppointmentOpt.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy lịch hẹn");
        }
        AppointmentEntity existingAppointment = existingAppointmentOpt.get();
        existingAppointment.setStatus(AppointmentStatus.CANCELLED);
        existingAppointment.setUpdatedAt(new Date());
        appointmentRepository.save(existingAppointment);
    }

    @Override
    public Optional<AppointmentResponseDTO> getAppointmentById(String id) {
        return appointmentRepository.findById(id)
                .map(appointmentConverter::toDTO);
    }


    @Override
    public boolean isTimeSlotAvailable(String postId, Date startTime, Date endTime) {
        return false;
    }

    @Override
    public void sendAppointmentReminder(String appointmentId) {

    }
}
