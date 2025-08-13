package com.rentzy.service.impl;

import com.rentzy.converter.NotificationConverter;
import com.rentzy.converter.UserNotificationSettingsConverter;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private NotificationRepository notificationRepository;
    private NotificationDeliveryRepository notificationDeliveryRepository;
    private UserNotificationSettingsRepository userNotificationSettingsRepository;
    private NotificationConverter notificationConverter;
    private UserNotificationSettingsConverter userNotificationSettingsConverter;
    private JavaMailSender javaMailSender;
    private UserRepository userRepository;
    private PostRepository postRepository;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");

    public static final String ACCOUNT_SID = System.getenv("ACCOUNT_SID");
    public static final String AUTH_TOKEN = System.getenv("AUTH_TOKEN");

    @Override
    public void sendAppointmentNotification(AppointmentEntity appointment, NotificationType type) {

        try {
            Map<String, Object> appointmentData = buildAppointmentData(appointment);

            switch (type){
                case APPOINTMENT_CREATED:
                    AppointmentCreatedNotification(appointment, appointmentData);
                    notifyOwnerNewRequest(appointment, appointmentData);
                    break;
                case APPOINTMENT_CONFIRMED:
                    AppointmentConfirmedNotification(appointment, appointmentData);
                    break;
                case APPOINTMENT_REJECTED:
                    AppointmentRejectNotification(appointment, appointmentData);
                    break;
                case APPOINTMENT_CANCELLED:
                    AppointmentCancelledNotification(appointment, appointmentData);
                    break;
                case APPOINTMENT_RESCHEDULED:
                    AppointmentRescheduledNotification(appointment, appointmentData);
                    break;
                case APPOINTMENT_COMPLETED:
                    AppointmentCompletedNotification(appointment, appointmentData);
                    break;
                default:
                    log.warn("Unknown notification type: {}", type);
            }
        }
        catch (Exception e) {
            log.error("Failed to send appointment notification: {}", e.getMessage(), e);
        }
    }

    @Override
    public void sendAppointmentReminder(AppointmentEntity appointment) {
        try {
            Map<String, Object> appointmentData = buildAppointmentData(appointment);

            NotificationRequestDTO notificationRequestDTO = NotificationRequestDTO.builder()
                    .userId(appointment.getUserId())
                    .title("Nhắc nhở lịch hẹn")
                    .message(String.format("Bạn có lịch hẹn vào %s (còn 1 giờ nữa)",
                            appointmentData.get("appointmentTime")))
                    .type(NotificationType.APPOINTMENT_REMINDER)
                    .priority(NotificationPriority.HIGH)
                    .relatedEntityId(appointment.getId())
                    .relatedEntityType("appointment")
                    .metadata(appointmentData)
                    .requiresAction(true)
                    .build();

            NotificationEntity notification = createNotification(notificationRequestDTO);
            deliverNotification(notification);
        }
        catch (Exception e) {
            log.error("Failed to send appointment reminder: {}", e.getMessage(), e);
        }
    }

    @Override
    @Async
    public void sendEmailNotification(String recipientEmail, String subject, String content) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(recipientEmail);
            message.setSubject(subject);
            message.setText(content);

            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity userEntity = userRepository.findByUsername(userName)
                    .orElseThrow(IllegalArgumentException::new);

            message.setFrom(userEntity.getEmail());

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
    public Page<NotificationResponseDTO> getUserNotifications(NotificationRequestDTO request) {
        List<NotificationEntity> notificationEntities = notificationRepository.findByUserIdOrderByCreatedAtDesc(request.getUserId());

        if (request.getType() != null){
            notificationEntities = notificationEntities.stream()
                    .filter(n -> n.getType().equals(request.getType()))
                    .collect(Collectors.toList());
        }

        if (request.getIsRead() != null){
            notificationEntities = notificationEntities.stream()
                    .filter(n -> n.isRead() == request.getIsRead())
                    .collect(Collectors.toList());
        }

        List<NotificationResponseDTO> notificationResponseDTOS = notificationEntities.stream()
                .map(notificationConverter::toDTO)
                .toList();

        return new PageImpl<>(notificationResponseDTOS);
    }

    @Override
    public void markAsRead(String notificationId) {
        NotificationEntity notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        notification.setRead(true);
        notification.setReadAt(LocalDateTime.now());
        notificationRepository.save(notification);
    }

    @Override
    public void markAllAsRead(String userId) {
        List<NotificationEntity> unReadNotifications = notificationRepository.findByUserIdAndReadFalseOrderByCreatedAtDesc(userId);

        unReadNotifications.forEach(notification -> {
            notification.setRead(true);
            notification.setReadAt(LocalDateTime.now());
        });

        notificationRepository.saveAll(unReadNotifications);
    }

    @Override
    public void bulkMarkAsRead(List<String> notificationIds) {
        List<NotificationEntity> notificationEntities = notificationRepository.findAllById(notificationIds);

        notificationEntities.forEach(notification -> {
            notification.setRead(true);
            notification.setReadAt(LocalDateTime.now());
        });

        notificationRepository.saveAll(notificationEntities);
    }

    @Override
    public UserNotificationSettingsResponseDTO getUserNotificationSettings(UserNotificationSettingsRequestDTO request){
        UserNotificationSettingsEntity userNotificationSettingsEntity = userNotificationSettingsRepository.findByUserId(request.getUserId())
                .orElse(createDefaultSettings(request.getUserId()));

        return userNotificationSettingsConverter.toDto(userNotificationSettingsEntity);
    }

    @Override
    public void deleteNotification(String notificationId) {
        try {

            if (notificationId == null || notificationId.trim().isEmpty()){
                throw new IllegalArgumentException("NotificationId cannot be null or empty");
            }

            NotificationEntity notification = notificationRepository.findById(notificationId).orElseThrow(
                    () -> new RuntimeException("Notification not found")
            );

            notificationDeliveryRepository.deleteById(notificationId);
            notificationRepository.delete(notification);

            log.info("Notification {} deleted", notificationId);
        }
        catch (Exception e){
            log.error("Failed to delete notification: {}", notificationId);
            throw new RuntimeException("Failed to delete notification", e);
        }
    }

    @Override
    public void deleteOldNotifications(int daysOld) {
        try {
            if (daysOld <= 0){
                throw new IllegalArgumentException("DaysOld cannot be less than 0");
            }

            Date cutoffDate = new Date(System.currentTimeMillis() - (daysOld * 24L * 60 * 60 * 1000));
            log.info("Deleting notifications older than {} days (before: {})", daysOld, cutoffDate);

            List<NotificationEntity> oldNotifications = notificationRepository.findByCreatedAtBefore(cutoffDate);
            if (oldNotifications.isEmpty()) {
                log.info("No old notifications found to delete");
                return;
            }

            List<String> notificationIds = oldNotifications.stream()
                    .map(NotificationEntity::getId)
                    .toList();

            for (String notificationId : notificationIds) {
                notificationDeliveryRepository.deleteById(notificationId);
            }

            notificationRepository.deleteAll(oldNotifications);

            log.info("Successfully deleted {} old notifications (older than {} days)", oldNotifications.size(), daysOld);
        }
        catch (Exception e){
            log.error("Failed to delete old notifications: {}", daysOld);
        }
    }

    @Override
    public void updateUserSettings(UserNotificationSettingsRequestDTO dto) {
        UserNotificationSettingsEntity settings = userNotificationSettingsRepository.findByUserId(dto.getUserId())
                .orElse(new UserNotificationSettingsEntity());

        settings.setUserId(dto.getUserId());
        settings.setEmailEnabled(dto.isEmailEnabled());
        settings.setSmsEnabled(dto.isSmsEnabled());
        settings.setPushEnabled(dto.isPushEnabled());
        settings.setInAppEnabled(dto.isInAppEnabled());
        settings.setTypePreferences(dto.getTypePreferences());
        settings.setTimezone(dto.getTimezone());
        settings.setQuietHoursStart(dto.getQuietHoursStart());
        settings.setQuietHoursEnd(dto.getQuietHoursEnd());
        settings.setUpdatedAt(LocalDateTime.now());

        userNotificationSettingsRepository.save(settings);
    }

    @Override
    public void processScheduledNotifications() {
        List<NotificationEntity> scheduledNotifications =
                notificationRepository.findByScheduledForBeforeAndReadFalse(new Date());

        scheduledNotifications.forEach(this::deliverNotification);
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

    private void notifyOwnerNewRequest(AppointmentEntity appointment, Map<String, Object> data) {
        NotificationRequestDTO notificationRequestDTO = NotificationRequestDTO.builder()
                .userId(appointment.getOwnerId())
                .title("Yêu cầu lịch hẹn mới")
                .message(String.format("%s muốn xem '%s' vào %s", data.get("guestName"), data.get("propertyName"), data.get("appointmentTime")))
                .type(NotificationType.APPOINTMENT_CREATED)
                .priority(NotificationPriority.HIGH)
                .relatedEntityId(appointment.getId())
                .relatedEntityType("appointment")
                .metadata(data)
                .requiresAction(true)
                .build();

        NotificationEntity notification = createNotification(notificationRequestDTO);
        deliverNotification(notification);
    }

    private void AppointmentConfirmedNotification(AppointmentEntity appointment, Map<String, Object> data) {
        NotificationRequestDTO notificationRequestDTO = NotificationRequestDTO.builder()
                .userId(appointment.getUserId())
                .title("Lịch hẹn đã được xác nhận")
                .message(String.format("Chủ nhà đã xác nhận lịch hẹn xem '%s' vào %s",
                        data.get("propertyName"), data.get("appointmentTime")))
                .type(NotificationType.APPOINTMENT_CONFIRMED)
                .priority(NotificationPriority.HIGH)
                .relatedEntityId(appointment.getId())
                .relatedEntityType("appointment")
                .metadata(data)
                .build();
        NotificationEntity notification = createNotification(notificationRequestDTO);
        deliverNotification(notification);
    }

    private void AppointmentRejectNotification(AppointmentEntity appointment, Map<String, Object> data) {
        String reason = Objects.toString(data.get("rejectionReason"), "Không có lý do cụ thể");
        NotificationRequestDTO notificationRequestDTO = NotificationRequestDTO.builder()
                .userId(appointment.getUserId())
                .title("Lịch hẹn đã bị từ chối")
                .message(String.format("Chủ nhà đã từ chối lịch hẹn xem '%s'. Lý do: %s",
                        data.get("propertyName"), reason))
                .type(NotificationType.APPOINTMENT_REJECTED)
                .priority(NotificationPriority.HIGH)
                .relatedEntityId(appointment.getId())
                .relatedEntityType("appointment")
                .metadata(data)
                .build();
        NotificationEntity notification = createNotification(notificationRequestDTO);
        deliverNotification(notification);
    }

    private void AppointmentCancelledNotification(AppointmentEntity appointment, Map<String, Object> data) {

        String reason = Objects.toString(data.get("cancellationReason"), "Không có lý do cụ thể");
        String cancelledBy = Objects.toString(data.getOrDefault("cancelledBy", "Hệ thống"));

        String[] userIds = {appointment.getUserId(), appointment.getOwnerId()};
        for (String userId : userIds){
            String message;
            if (userId.equals(appointment.getUserId())) {
                message = String.format("Lịch hẹn xem '%s' vào %s đã bị hủy bởi %s. Lý do: %s",
                        data.get("propertyName"), data.get("appointmentTime"), cancelledBy, reason);
            }
            else {
                message = String.format("Lịch hẹn của %s xem '%s' vào %s đã bị hủy. Lý do: %s",
                        data.get("guestName"), data.get("propertyName"), data.get("appointmentTime"), reason);
            }

            NotificationRequestDTO notificationRequestDTO = NotificationRequestDTO.builder()
                    .userId(userId)
                    .title("Lịch hẹn đã bị hủy")
                    .message(message)
                    .type(NotificationType.APPOINTMENT_CANCELLED)
                    .priority(NotificationPriority.HIGH)
                    .relatedEntityId(appointment.getId())
                    .relatedEntityType("appointment")
                    .metadata(data)
                    .build();
            NotificationEntity notification = createNotification(notificationRequestDTO);
            deliverNotification(notification);
        }
    }

    private void AppointmentRescheduledNotification(AppointmentEntity appointment, Map<String, Object> data) {
        String[] userIds = {appointment.getUserId(), appointment.getOwnerId()};
        for (String userId : userIds){
            String message;
            if (userId.equals(appointment.getUserId())) {
                message = String.format("Lịch hẹn xem '%s' đã được dời đến %s",
                        data.get("propertyName"), data.get("appointmentTime"));
            }
            else {
                message = String.format("Lịch hẹn của %s xem '%s' đã được dời đến %s",
                        data.get("guestName"), data.get("propertyName"), data.get("appointmentTime"));
            }

            NotificationRequestDTO notificationRequestDTO = NotificationRequestDTO.builder()
                    .userId(userId)
                    .title("Thông báo dời lịch hẹn")
                    .message(message)
                    .type(NotificationType.APPOINTMENT_RESCHEDULED)
                    .priority(NotificationPriority.HIGH)
                    .relatedEntityId(appointment.getId())
                    .relatedEntityType("appointment")
                    .metadata(data)
                    .build();
            NotificationEntity notification = createNotification(notificationRequestDTO);
            deliverNotification(notification);
        }
    }

    private void AppointmentCompletedNotification(AppointmentEntity appointment, Map<String, Object> data) {
        String[] userIds = {appointment.getUserId(), appointment.getOwnerId()};
        String message;
        for (String userId : userIds){
            if (userId.equals(appointment.getUserId())) {
                message = String.format("Bạn đã hoàn thành việc xem '%s'. Hãy để lại đánh giá của bạn!",
                        data.get("propertyName"));
            }
            else {
                message = String.format("Lịch hẹn với %s xem '%s' đã hoàn thành thành công",
                        data.get("guestName"), data.get("propertyName"));
            }

            NotificationRequestDTO notificationRequestDTO = NotificationRequestDTO.builder()
                    .userId(userId)
                    .title("Lịch hẹn đã hoàn thành")
                    .message(message)
                    .type(NotificationType.APPOINTMENT_COMPLETED)
                    .priority(NotificationPriority.MEDIUM)
                    .relatedEntityId(appointment.getId())
                    .relatedEntityType("appointment")
                    .metadata(data)
                    .build();
            NotificationEntity notification = createNotification(notificationRequestDTO);
            deliverNotification(notification);
        }
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
        data.put("rejectionReason", appointment.getRejectionReason());
        data.put("cancellationReason", appointment.getCancellationReason());
        data.put("cancelledBy", appointment.getCancelledBy());
        return data;
    }
}
