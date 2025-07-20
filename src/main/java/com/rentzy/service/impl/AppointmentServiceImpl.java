package com.rentzy.service.impl;

import com.rentzy.converter.AppointmentConverter;
import com.rentzy.entity.AppointmentEntity;
import com.rentzy.enums.AppointmentStatus;
import com.rentzy.model.dto.request.AppointmentRequestDTO;
import com.rentzy.model.dto.response.AppointmentResponseDTO;
import com.rentzy.repository.AppointmentRepository;
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

    private static final int MIN_ADVANCE_BOOKING_HOURS = 2;
    private static final int BUSINESS_HOUR_START = 9;
    private static final int BUSINESS_HOUR_END = 18;
    private static final int DEFAULT_APPOINTMENT_DURATION = 60;

    @Override
    public AppointmentResponseDTO createAppointment(AppointmentRequestDTO request) {

        return null;
    }

    @Override
    public Optional<AppointmentResponseDTO> getAppointmentById(String id) {
        return Optional.empty();
    }

    @Override
    public AppointmentResponseDTO updateAppointment(String id, AppointmentRequestDTO request) {
        return null;
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
    public void cancelAppointment(String id) {

    }

    @Override
    public void rejectAppointment(String id) {

    }

    @Override
    public void completeAppointment(String id) {

    }

    @Override
    public AppointmentResponseDTO rescheduleAppointment(String id, AppointmentRequestDTO request) {
        return null;
    }

    @Override
    public boolean isTimeSlotAvailable(String postId, Date startTime, Date endTime) {
        return false;
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

    private void validateNoConflict(AppointmentRequestDTO request) {
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
}
