package com.rentzy.service.impl;

import com.rentzy.converter.NotificationConverter;
import com.rentzy.entity.AppointmentEntity;
import com.rentzy.entity.NotificationEntity;
import com.rentzy.entity.PostEntity;
import com.rentzy.entity.UserEntity;
import com.rentzy.enums.notification.NotificationPriority;
import com.rentzy.enums.notification.NotificationRecipient;
import com.rentzy.enums.notification.NotificationType;
import com.rentzy.model.dto.request.NotificationRequestDTO;
import com.rentzy.model.dto.request.UserNotificationSettingsRequestDTO;
import com.rentzy.model.dto.response.NotificationResponseDTO;
import com.rentzy.model.dto.response.UserNotificationSettingsResponseDTO;
import com.rentzy.repository.*;
import com.rentzy.service.NotificationService;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {

    private NotificationRepository notificationRepository;
    private NotificationDeliveryRepository notificationDeliveryRepository;
    private UserNotificationSettingsRepository userNotificationSettingsRepository;
    private NotificationConverter notificationConverter;
    private JavaMailSender javaMailSender;
    private UserRepository userRepository;
    private PostRepository postRepository;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");

    @Override
    public void sendAppointmentNotification(AppointmentEntity appointment, NotificationType type) {
        Map<String, Object> appointmentData = buildAppointmentData(appointment);

        switch (type){
            case APPOINTMENT_CREATED:

        }
    }

    @Override
    public void sendAppointmentReminder(AppointmentEntity appointment) {

    }

    @Override
    public void sendEmailNotification(NotificationRecipient recipient, String subject, String content) {

    }

    @Override
    public void sendSMSNotification(String phongNumber, String content) {

    }

    @Override
    public void sendPushNotification(String userId, String title, String message) {

    }

    @Override
    public NotificationEntity createNotification(NotificationRequestDTO requestDTO) {
        NotificationEntity notificationEntity = new NotificationEntity();
        notificationEntity.setUserId(requestDTO.getUserId());
        notificationEntity.setTitle(requestDTO.getTitle());
        notificationEntity.setMessage(requestDTO.getMessage());
        notificationEntity.setType(requestDTO.getType());
        notificationEntity.setPriority(requestDTO.getPriority() != null ? requestDTO.getPriority() : NotificationPriority.MEDIUM);
        notificationEntity.setRelatedEntityId(requestDTO.getRelatedEntityId());
        notificationEntity.setRelatedEntityType(requestDTO.getRelatedEntityType());
        notificationEntity.setMetadata(requestDTO.getMetadata());

        return notificationRepository.save(notificationEntity);
    }

    @Override
    public Page<NotificationResponseDTO> getUserNotifications(String userId, Pageable pageable) {
        return null;
    }

    @Override
    public void markAsRead(String notificationId) {

    }

    @Override
    public void markAllAsRead(String userId) {

    }

    @Override
    public void bulkMarkAsRead(List<String> notificationIds) {

    }

    @Override
    public void deleteNotification(String notificationId) {

    }

    @Override
    public void deleteOldNotifications(int daysOld) {

    }

    @Override
    public UserNotificationSettingsResponseDTO getUserNotificationSettings(String userId) {
        return null;
    }

    @Override
    public void updateUserSettings(String userId, UserNotificationSettingsRequestDTO request) {

    }

    @Override
    public void processScheduledNotifications() {

    }

    @Override
    public void retryFailedDeliveries() {

    }

    private void AppointmentCreatedNotification(AppointmentEntity appointment, Map<String, Object> data){
        NotificationRequestDTO notificationRequestDTO = NotificationRequestDTO.builder()
                .userId(appointment.getUserId())
                .title("Lịch hẹn đã được tạo")
                .message(String.format("Lịch hẹn xem '%s' vào %s đã được gửi thành công",
                        data.get("propertyName"), data.get("appointmentTime")))
                .type(NotificationType.APPOINTMENT_CREATED)
                .relatedEntityId(appointment.getId())
                .relatedEntityType("appointment")
                .metadata(data)
                .build();

        NotificationEntity notification = createNotification(notificationRequestDTO);
        //deliverNotification
    }

    private Map<String, Object> buildAppointmentData(AppointmentEntity appointment) {
        Map<String, Object> data = new HashMap<>();
        data.put("appointmentId", appointment.getId());
        data.put("appointmentTime", sdf.format(appointment.getAppointmentDate()));
        data.put("status", appointment.getStatus());
        data.put("duration", appointment.getDurationMinutes() + " minutes");

        UserEntity owner = userRepository.findById(appointment.getOwnerId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy owner với ID: " + appointment.getOwnerId()));

        UserEntity guest = userRepository.findById(appointment.getUserId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy guest với ID: " + appointment.getUserId()));

        PostEntity post = postRepository.findById(appointment.getPostId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài đăng với ID: " + appointment.getPostId()));

        data.put("ownerName", owner.getFullName());
        data.put("guestName", guest.getFullName());
        data.put("propertyName", post.getPropertyName());
        return data;
    }
}
