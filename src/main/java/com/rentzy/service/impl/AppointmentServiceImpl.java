package com.rentzy.service.impl;

import com.rentzy.converter.AppointmentConverter;
import com.rentzy.entity.AppointmentEntity;
import com.rentzy.entity.PostEntity;
import com.rentzy.enums.AppointmentStatus;
import com.rentzy.enums.notification.NotificationType;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentConverter appointmentConverter;
    private final NotificationService notificationService;
    private final PostRepository postRepository;

    private static final int MIN_ADVANCE_BOOKING_HOURS = 2;
    private static final int BUSINESS_HOUR_START = 9;
    private static final int BUSINESS_HOUR_END = 18;
    private static final int DEFAULT_APPOINTMENT_DURATION = 60;

    @Override
    @Transactional
    public AppointmentResponseDTO createAppointment(AppointmentRequestDTO request) {

        Optional<PostEntity> post = postRepository.findById(request.getPostId());
        if (post.isEmpty()){
            throw new IllegalArgumentException("Post is not found");
        }

        if (request.getUserId().equals(post.get().getCreatedBy())) {
            throw new IllegalArgumentException("Không thể đặt lịch với chính mình");
        }

        validateAppointmentTime(request.getAppointmentDate());
        validateNoConflict(request);

        AppointmentEntity appointmentEntity = appointmentConverter.toEntity(request);
        appointmentEntity.setOwnerId(post.get().getCreatedBy());
        appointmentEntity.setStatus(AppointmentStatus.PENDING);
        appointmentEntity.setReminderSent(false);

        AppointmentEntity savedEntity = appointmentRepository.save(appointmentEntity);

        notificationService.sendAppointmentNotification(savedEntity, NotificationType.APPOINTMENT_CREATED);
        return appointmentConverter.toDTO(savedEntity);
    }

    @Override
    public Optional<AppointmentResponseDTO> getAppointmentById(String id) {
        return appointmentRepository.findById(id).map(appointmentConverter::toDTO);
    }

    @Override
    public Page<AppointmentResponseDTO> getAppointmentsByPostId(String postId, Pageable pageable) {
        return appointmentRepository.findByPostId(postId, pageable).map(appointmentConverter::toDTO);
    }

    @Override
    public Page<AppointmentResponseDTO> getAppointmentsByUserId(String userId, Pageable pageable) {
        return appointmentRepository.findByUserId(userId, pageable).map(appointmentConverter::toDTO);
    }

    @Override
    public Page<AppointmentResponseDTO> getAppointmentsByOwnerId(String ownerId, Pageable pageable) {
        return appointmentRepository.findByOwnerId(ownerId, pageable).map(appointmentConverter::toDTO);
    }

    @Override
    public Page<AppointmentResponseDTO> getAppointmentsByDate(Date date, Pageable pageable) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);

        Date start = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH,1);
        Date end = calendar.getTime();

        return appointmentRepository.findByAppointmentDateBetween(start,end,pageable).map(appointmentConverter::toDTO);
    }

    @Override
    public AppointmentResponseDTO confirmAppointment(String appointmentId, String ownerId) {
        AppointmentEntity appointmentEntity = appointmentRepository.findById(appointmentId).orElse(null);
        if (appointmentEntity == null) {
            throw new IllegalArgumentException("Appointment is not found");
        }

        if (!isOwner(appointmentEntity, ownerId)){
            throw new IllegalArgumentException("only owner can confirm appointment");
        }

        if (appointmentEntity.getStatus() != AppointmentStatus.PENDING){
            throw new IllegalArgumentException("Can only confirm pending appointments. Current status: " + appointmentEntity.getStatus());
        }

        appointmentEntity.setStatus(AppointmentStatus.CONFIRMED);
        appointmentEntity.setConfirmedAt(new Date());
        appointmentEntity.setUpdatedAt(new Date());

        AppointmentEntity savedEntity = appointmentRepository.save(appointmentEntity);

        notificationService.sendAppointmentNotification(savedEntity, NotificationType.APPOINTMENT_CONFIRMED);
        return appointmentConverter.toDTO(savedEntity);
    }

    @Override
    public AppointmentResponseDTO cancelAppointment(String appointmentId, String currentUserId, String reason) {
        AppointmentEntity appointmentEntity = appointmentRepository.findById(appointmentId).orElse(null);
        if (appointmentEntity == null) {
            throw new IllegalArgumentException("Appointment is not found");
        }

        if (!isOwner(appointmentEntity, currentUserId) && !isUser(appointmentEntity, currentUserId)){
            throw new IllegalArgumentException("you are not authorized to cancel this appointment");
        }

        if (appointmentEntity.getStatus() != AppointmentStatus.PENDING){
            throw new IllegalArgumentException("Can only cancel pending appointments. Current status: " + appointmentEntity.getStatus());
        }

        appointmentEntity.setStatus(AppointmentStatus.CANCELLED);
        appointmentEntity.setCancellationReason(reason);
        appointmentEntity.setUpdatedAt(new Date());

        AppointmentEntity savedEntity = appointmentRepository.save(appointmentEntity);

        notificationService.sendAppointmentNotification(savedEntity, NotificationType.APPOINTMENT_CANCELLED);
        return appointmentConverter.toDTO(savedEntity);
    }

    @Override
    public AppointmentResponseDTO rejectAppointment(String appointmentId, String ownerId, String reason) {
        AppointmentEntity appointmentEntity = appointmentRepository.findById(appointmentId).orElse(null);
        if (appointmentEntity == null) {
            throw new IllegalArgumentException("Appointment is not found");
        }

        if (!isOwner(appointmentEntity, ownerId)){
            throw new IllegalArgumentException("only owner can reject appointment");
        }

        if (appointmentEntity.getStatus() != AppointmentStatus.PENDING){
            throw new IllegalArgumentException("Can only confirm pending appointments. Current status: " + appointmentEntity.getStatus());
        }

        appointmentEntity.setStatus(AppointmentStatus.REJECTED);
        appointmentEntity.setRejectionReason(reason);
        appointmentEntity.setUpdatedAt(new Date());

        AppointmentEntity savedEntity = appointmentRepository.save(appointmentEntity);

        notificationService.sendAppointmentNotification(savedEntity, NotificationType.APPOINTMENT_REJECTED);
        return appointmentConverter.toDTO(savedEntity);
    }

    @Override
    public AppointmentResponseDTO completeAppointment(String appointmentId, String ownerId) {
        AppointmentEntity appointmentEntity = appointmentRepository.findById(appointmentId).orElse(null);
        if (appointmentEntity == null) {
            throw new IllegalArgumentException("Appointment is not found");
        }

        if (!isOwner(appointmentEntity, ownerId)){
            throw new IllegalArgumentException("only owner can complete appointment");
        }

        if (appointmentEntity.getStatus() != AppointmentStatus.CONFIRMED){
            throw new IllegalArgumentException("Can only complete confirmed appointments. Current status: " + appointmentEntity.getStatus());
        }

        appointmentEntity.setStatus(AppointmentStatus.COMPLETED);
        appointmentEntity.setCompletedAt(new Date());
        appointmentEntity.setUpdatedAt(new Date());

        AppointmentEntity savedEntity = appointmentRepository.save(appointmentEntity);

        notificationService.sendAppointmentNotification(savedEntity, NotificationType.APPOINTMENT_COMPLETED);
        return appointmentConverter.toDTO(savedEntity);
    }

    @Override
    public AppointmentResponseDTO rescheduleAppointment(AppointmentRequestDTO request) {
        AppointmentEntity appointmentEntity = appointmentRepository.findById(request.getAppointmentId()).orElse(null);
        if (appointmentEntity == null) {
            throw new IllegalArgumentException("Appointment is not found");
        }

        if (!isOwner(appointmentEntity, request.getUserId()) && !isUser(appointmentEntity, request.getUserId())){
            throw new IllegalArgumentException("you are not authorized to reschedule this appointment");
        }

        if (appointmentEntity.getStatus() == AppointmentStatus.COMPLETED
                || appointmentEntity.getStatus() == AppointmentStatus.CANCELLED){
            throw new IllegalArgumentException("Cannot reschedule completed or cancelled appointment");
        }

        validateAppointmentTime(request.getAppointmentDate());
        int duration = request.getDurationMinutes() != null ? request.getDurationMinutes() : DEFAULT_APPOINTMENT_DURATION;
        validateNoConflict(request);

        appointmentEntity.setAppointmentDate(request.getAppointmentDate());
        appointmentEntity.setDurationMinutes(duration);
        appointmentEntity.setStatus(AppointmentStatus.PENDING);
        appointmentEntity.setUpdatedAt(new Date());

        AppointmentEntity savedEntity = appointmentRepository.save(appointmentEntity);

        notificationService.sendAppointmentNotification(savedEntity, NotificationType.APPOINTMENT_RESCHEDULED);
        return appointmentConverter.toDTO(savedEntity);
    }

    @Override
    public void sendAppointmentReminder() {
        Date now = new Date();
        Date tomorrow = new Date(now.getTime() + TimeUnit.HOURS.toMillis(24));

        List<AppointmentEntity> upcomingAppointments = appointmentRepository.findUpcomingAppointments(now, tomorrow, AppointmentStatus.CONFIRMED);

        for(AppointmentEntity appointment : upcomingAppointments){
            if (!appointment.getReminderSent()){
                long timeDiff = appointment.getAppointmentDate().getTime() - now.getTime();
                if(timeDiff <= TimeUnit.HOURS.toMillis(2) && timeDiff > 0){
                    notificationService.sendAppointmentReminder(appointment);

                    appointment.setReminderSent(true);
                    appointment.setUpdatedAt(now);
                    appointmentRepository.save(appointment);
                }
            }
        }
    }

    private void validateAppointmentTime(Date appointmentTime) {
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

    private void validateNoConflict(AppointmentRequestDTO request) {
        Date startTime = request.getAppointmentDate();
        int duration =  request.getDurationMinutes() != null ? request.getDurationMinutes() : DEFAULT_APPOINTMENT_DURATION;
        Date endTime = new Date(startTime.getTime() + TimeUnit.MINUTES.toMillis(duration));

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

    private boolean isOwner(AppointmentEntity appointment, String userId){
        return appointment.getOwnerId().equals(userId);
    }

    private boolean isUser(AppointmentEntity appointment, String userId){
        return appointment.getUserId().equals(userId);
    }
}
