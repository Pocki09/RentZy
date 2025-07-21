package com.rentzy.service.impl;

import com.rentzy.converter.AppointmentConverter;
import com.rentzy.entity.AppointmentEntity;
import com.rentzy.entity.PostEntity;
import com.rentzy.enums.AppointmentStatus;
import com.rentzy.model.dto.request.AppointmentRequestDTO;
import com.rentzy.model.dto.response.AppointmentResponseDTO;
import com.rentzy.repository.AppointmentRepository;
import com.rentzy.repository.PostRepository;
import com.rentzy.service.AppointmentService;
import com.rentzy.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private AppointmentRepository appointmentRepository;
    private AppointmentConverter appointmentConverter;
    private NotificationService notificationService;
    private PostRepository postRepository;

    private static final int MIN_ADVANCE_BOOKING_HOURS = 2;
    private static final int BUSINESS_HOUR_START = 9;
    private static final int BUSINESS_HOUR_END = 18;
    private static final int DEFAULT_APPOINTMENT_DURATION = 60;

    @Override
    public AppointmentResponseDTO createAppointment(AppointmentRequestDTO request) {

        Optional<PostEntity> post = postRepository.findById(request.getPostId());
        if (post.isEmpty()){
            throw new IllegalArgumentException("Post is not found");
        }

        validateAppointmentTime(request.getAppointmentDate());
        validateNoConflict(request);

        AppointmentEntity appointmentEntity = appointmentConverter.toEntity(request);
        appointmentEntity.setOwnerId(post.get().getCreatedBy());
        appointmentEntity.setStatus(AppointmentStatus.PENDING);
        appointmentEntity.setReminderSent(false);

        AppointmentEntity savedEntity = appointmentRepository.save(appointmentEntity);
        return appointmentConverter.toDTO(savedEntity);
    }

    @Override
    public Optional<AppointmentResponseDTO> getAppointmentById(String id) {
        return Optional.empty();
    }


    @Override
    public Page<AppointmentResponseDTO> getAppointmentsByPostId(String postId, Pageable pageable) {
        return null;
    }

    @Override
    public Page<AppointmentResponseDTO> getAppointmentsByUserId(String userId, Pageable pageable) {
        return null;
    }

    @Override
    public Page<AppointmentResponseDTO> getAppointmentsByOwnerId(String ownerId, Pageable pageable) {
        return null;
    }

    @Override
    public Page<AppointmentResponseDTO> getAppointmentsByDate(Date date, Pageable pageable) {
        return null;
    }

    @Override
    public AppointmentResponseDTO confirmAppointment(String appointmentId, String ownerId) {
        AppointmentEntity appointmentEntity = appointmentRepository.findById(appointmentId).orElse(null);
        if (appointmentEntity == null) {
            throw new IllegalArgumentException("Appointment is not found");
        }

        if (!isOwner(appointmentId, ownerId)){
            throw new IllegalArgumentException("only owner can confirm appointment");
        }

        if (appointmentEntity.getStatus() != AppointmentStatus.PENDING){
            throw new IllegalArgumentException("Can only confirm pending appointments. Current status: " + appointmentEntity.getStatus());
        }

        appointmentEntity.setStatus(AppointmentStatus.CONFIRMED);
        appointmentEntity.setConfirmedAt(new Date());
        appointmentEntity.setUpdatedAt(new Date());

        AppointmentEntity savedEntity = appointmentRepository.save(appointmentEntity);
        return appointmentConverter.toDTO(savedEntity);
    }

    @Override
    public AppointmentResponseDTO cancelAppointment(String appointmentId, String ownerId, String reason) {
        // Thiếu xét phải là cuộc hẹn giữa chính owner và user đó
        AppointmentEntity appointmentEntity = appointmentRepository.findById(appointmentId).orElse(null);
        if (appointmentEntity == null) {
            throw new IllegalArgumentException("Appointment is not found");
        }

        if (appointmentEntity.getStatus() != AppointmentStatus.PENDING){
            throw new IllegalArgumentException("Can only confirm pending appointments. Current status: " + appointmentEntity.getStatus());
        }

        appointmentEntity.setStatus(AppointmentStatus.CANCELLED);
        appointmentEntity.setCancellationReason(reason);
        appointmentEntity.setUpdatedAt(new Date());

        AppointmentEntity savedEntity = appointmentRepository.save(appointmentEntity);
        return appointmentConverter.toDTO(savedEntity);
    }

    @Override
    public AppointmentResponseDTO rejectAppointment(String appointmentId, String ownerId, String reason) {
        AppointmentEntity appointmentEntity = appointmentRepository.findById(appointmentId).orElse(null);
        if (appointmentEntity == null) {
            throw new IllegalArgumentException("Appointment is not found");
        }

        if (!isOwner(appointmentId, ownerId)){
            throw new IllegalArgumentException("only owner can reject appointment");
        }

        if (appointmentEntity.getStatus() != AppointmentStatus.PENDING){
            throw new IllegalArgumentException("Can only confirm pending appointments. Current status: " + appointmentEntity.getStatus());
        }

        appointmentEntity.setStatus(AppointmentStatus.REJECTED);
        appointmentEntity.setRejectionReason(reason);
        appointmentEntity.setUpdatedAt(new Date());

        AppointmentEntity savedEntity = appointmentRepository.save(appointmentEntity);
        return appointmentConverter.toDTO(savedEntity);
    }

    @Override
    public AppointmentResponseDTO completeAppointment(String appointmentId, String ownerId) {
        return null;
    }

    @Override
    public AppointmentResponseDTO rescheduleAppointment(String id, AppointmentRequestDTO request) {
        return null;
    }

    @Override
    public void sendAppointmentReminder(String appointmentId) {

    }

    void validateAppointmentTime(Date appointmentTime) {
        Date now = new Date();

        if (appointmentTime.before(now)) {
            throw new IllegalArgumentException("Appointment date cannot be in the past");
        }

        long timeDiff = appointmentTime.getTime() - now.getTime();
        if (timeDiff < TimeUnit.HOURS.toMillis(MIN_ADVANCE_BOOKING_HOURS)) {
            throw new IllegalArgumentException("Appointment must be booked at least " + MIN_ADVANCE_BOOKING_HOURS + " hours in advance");
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(appointmentTime);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        if (hour < BUSINESS_HOUR_START || hour >= BUSINESS_HOUR_END) {
            throw new IllegalArgumentException("Appointment must be between " + BUSINESS_HOUR_START + "AM and " + BUSINESS_HOUR_END + "PM");
        }

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
            throw new IllegalArgumentException("Appointments are not available on weekends");
        }
    }

    void validateNoConflict(AppointmentRequestDTO request) {
        Date startTime = request.getAppointmentDate();
        Date endTime = new Date(startTime.getTime() + TimeUnit.MINUTES.toMillis(request.getDurationMinutes()));

        List<AppointmentEntity> conflictingAppointments = appointmentRepository.findConflictingAppointments(request.getPostId(), startTime, AppointmentStatus.CONFIRMED, AppointmentStatus.PENDING);

        for (AppointmentEntity existing : conflictingAppointments) {
            if (request.getAppointmentId() != null && existing.getId().equals(request.getAppointmentId())) {
                continue;
            }

            Date existingStartTime = existing.getAppointmentDate();
            int existingDuration = existing.getDurationMinutes() != null? existing.getDurationMinutes() : DEFAULT_APPOINTMENT_DURATION;
            Date existingEndTime = new Date(existingStartTime.getTime() + TimeUnit.MINUTES.toMillis(existingDuration));

            if (startTime.before(existingEndTime) && endTime.after(existingStartTime)) {
                throw new IllegalArgumentException("Time slot conflicts with existing appointment");
            }
        }
    }

    private boolean isOwner(String appointmentId, String userId){
        Optional<AppointmentEntity> appointment = appointmentRepository.findById(appointmentId);
        if (appointment.isEmpty()){
            return false;
        }

        AppointmentEntity appointmentEntity = appointment.get();
        return appointmentEntity.getOwnerId().equals(userId);
    }
}
