package com.rentzy.service.impl;

import com.rentzy.converter.AppointmentConverter;
import com.rentzy.enums.AppointmentStatus;
import com.rentzy.model.dto.AppointmentDTO;
import com.rentzy.model.dto.AppointmentSearchDTO;
import com.rentzy.model.entity.AppointmentEntity;
import com.rentzy.repository.AppointmentRepository;
import com.rentzy.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private AppointmentConverter appointmentConverter;

    @Override
    public AppointmentDTO createAppointment(AppointmentDTO appointmentDTO) {
        if (!isTimeSlotAvailable(appointmentDTO.getPostId(),appointmentDTO.getStartDate(),appointmentDTO.getEndDate())){
            throw new IllegalArgumentException("Khung giờ này đã có lịch hẹn, vui lòng chọn thời gian khác.");
        }

        AppointmentEntity appointmentEntity = appointmentConverter.toEntity(appointmentDTO);
        appointmentEntity = appointmentRepository.save(appointmentEntity);
        return appointmentConverter.toDTO(appointmentEntity);
    }

    @Override
    public AppointmentDTO updateAppointment(String id, AppointmentDTO appointmentDTO) {
        Optional<AppointmentEntity> appointmentEntity = appointmentRepository.findById(id);
        if(appointmentEntity.isEmpty()){
            throw new IllegalArgumentException("Không tìm thấy lịch hẹn với ID: " + id);
        }

        AppointmentEntity existingAppointment = appointmentEntity.get();

        if (!existingAppointment.getStartDate().equals(appointmentDTO.getStartDate())
        || !existingAppointment.getEndDate().equals(appointmentDTO.getEndDate())) {
            if (!isTimeSlotAvailable(appointmentDTO.getPostId(),appointmentDTO.getStartDate(),appointmentDTO.getEndDate())){
                throw new IllegalArgumentException("Khung giờ này đã có lịch hẹn, vui lòng chọn thời gian khác.");
            }
        }

        existingAppointment.setStartDate(appointmentDTO.getStartDate());
        existingAppointment.setEndDate(appointmentDTO.getEndDate());
        existingAppointment.setStatus(appointmentDTO.getStatus());

        appointmentRepository.save(existingAppointment);
        return appointmentConverter.toDTO(existingAppointment);
    }

    @Override
    public void cancelAppointment(String id) {
        Optional<AppointmentEntity> existingAppointmentOpt = appointmentRepository.findById(id);
        if (existingAppointmentOpt.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy lịch hẹn với ID: " + id);
        }
        AppointmentEntity existingAppointment = existingAppointmentOpt.get();
        existingAppointment.setStatus(AppointmentStatus.CANCELLED);
        existingAppointment.setUpdatedAt(new Date());
        appointmentRepository.save(existingAppointment);
    }

    @Override
    public Optional<AppointmentDTO> getAppointmentById(String id) {
        return appointmentRepository.findById(id)
                .map(appointmentConverter::toDTO);
    }

    @Override
    public Page<AppointmentDTO> searchAppointments(AppointmentSearchDTO searchCriteria, Pageable pageable) {
        Page<AppointmentEntity> entities = appointmentRepository.searchAppointments(searchCriteria, pageable);
        return entities.map(appointmentConverter::toDTO);
    }

    @Override
    public boolean isTimeSlotAvailable(String postId, Date startTime, Date endTime) {
        return appointmentRepository.isTimeSlotAvailable(postId, startTime, endTime);
    }

    @Override
    public void sendAppointmentReminder(String appointmentId) {

    }
}
