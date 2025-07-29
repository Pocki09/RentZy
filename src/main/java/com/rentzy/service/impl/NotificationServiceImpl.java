package com.rentzy.service.impl;

import com.rentzy.converter.NotificationConverter;
import com.rentzy.entity.*;
import com.rentzy.enums.notification.NotificationChanel;
import com.rentzy.enums.notification.NotificationPriority;
import com.rentzy.enums.notification.NotificationType;
import com.rentzy.model.dto.request.NotificationRequestDTO;
import com.rentzy.model.dto.request.UserNotificationSettingsRequestDTO;
import com.rentzy.model.dto.response.NotificationResponseDTO;
import com.rentzy.model.dto.response.UserNotificationSettingsResponseDTO;
import com.rentzy.repository.*;
import com.rentzy.service.NotificationService;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Service
@Slf4j
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
                AppointmentCreatedNotification(appointment, appointmentData);

        }
    }

    @Override
    public void sendAppointmentReminder(AppointmentEntity appointment) {

    }

    @Override
    @Async
    public void sendEmailNotification(String recipientEmail, String subject, String content) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(recipientEmail);
            message.setSubject(subject);
            message.setText(content);
            message.setFrom("hanhatminh12a13@gmail.com");

            javaMailSender.send(message);
            log.info("Email sent successfully to: {}", recipientEmail);
        }
        catch (Exception e){
            log.error("Failed to send email to {}: {}", recipientEmail, e.getMessage());
        }
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
        deliverNotification(notification);
    }

    private void deliverNotification(NotificationEntity notificationEntity){
        UserNotificationSettingsEntity settings =
                userNotificationSettingsRepository.findByUserId(notificationEntity.getUserId())
                        .orElse(createDefaultSettings(notificationEntity.getUserId()));

        if (settings.isInAppEnabled()){
            log.info("In-app notification delivered for user: {}", notificationEntity.getUserId());
        }

        if (settings.isEmailEnabled()){
            UserEntity user = userRepository.findById(notificationEntity.getUserId()).
                    orElseThrow(() -> new RuntimeException("User not found"));
            String email = user.getEmail();
            if (email != null){
                createDeliveryRecord(notificationEntity.getId(), notificationEntity.getUserId(), NotificationChanel.EMAIL, email);
                sendEmailNotification(email, notificationEntity.getTitle(), notificationEntity.getMessage());
            }
        }

        if (settings.isPushEnabled()) {
            createDeliveryRecord(notificationEntity.getId(), notificationEntity.getUserId(), NotificationChanel.PUSH, notificationEntity.getUserId());
            sendPushNotification(notificationEntity.getUserId(), notificationEntity.getTitle(), notificationEntity.getMessage());
        }

        if (settings.isSmsEnabled()) {
            UserEntity user = userRepository.findById(notificationEntity.getUserId()).
                    orElseThrow(() -> new RuntimeException("User not found"));
            String phone = user.getPhone();
            if (phone != null){
                createDeliveryRecord(notificationEntity.getId(), notificationEntity.getUserId(), NotificationChanel.SMS, phone);
                sendSMSNotification(phone, notificationEntity.getMessage());
            }
        }
    }

    private void createDeliveryRecord(String notificationId, String userId, NotificationChanel chanel, String recipient){
        NotificationDeliveryEntity notificationDeliveryEntity = new NotificationDeliveryEntity();
        notificationDeliveryEntity.setNotificationId(notificationId);
        notificationDeliveryEntity.setUserId(userId);
        notificationDeliveryEntity.setChanel(chanel);
        notificationDeliveryEntity.setRecipient(recipient);

        notificationDeliveryRepository.save(notificationDeliveryEntity);
    }

    private UserNotificationSettingsEntity createDefaultSettings(String userId) {
        UserNotificationSettingsEntity settings = new UserNotificationSettingsEntity();
        settings.setUserId(userId);

        Map<String, Boolean> typePreferences = new HashMap<>();
        typePreferences.put("APPOINTMENT_CREATED", true);
        typePreferences.put("APPOINTMENT_CONFIRMED", true);
        typePreferences.put("APPOINTMENT_REJECTED", true);
        typePreferences.put("APPOINTMENT_CANCELLED", true);
        typePreferences.put("APPOINTMENT_REMINDER", true);
        settings.setTypePreferences(typePreferences);

        return userNotificationSettingsRepository.save(settings);
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
