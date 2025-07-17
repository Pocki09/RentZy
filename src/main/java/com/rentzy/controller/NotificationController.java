package com.rentzy.controller;

import com.rentzy.exception.ResourceNotFoundException;
import com.rentzy.model.dto.FilterParams;
import com.rentzy.model.dto.NotificationDTO;
import com.rentzy.service.NotificationService;
import com.rentzy.utils.PageableUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<Page<NotificationDTO>> getAllNotifications(
            @Valid @ModelAttribute FilterParams filterParams) {
        Pageable pageable = PageableUtils.buildPageable(filterParams, 10);
        Page<NotificationDTO> page = notificationService.getAllNotifications(pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<NotificationDTO>> getNotificationsByUser(
            @PathVariable String userId,
            @Valid @ModelAttribute FilterParams filterParams) {
        Pageable pageable = PageableUtils.buildPageable(filterParams, 10);
        Page<NotificationDTO> page = notificationService.getNotificationsByUserId(userId, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<Page<NotificationDTO>> getUnreadNotifications(
            @PathVariable String userId,
            @Valid @ModelAttribute FilterParams filterParams) {
        Pageable pageable = PageableUtils.buildPageable(filterParams, 10);
        Page<NotificationDTO> page = notificationService.getUnreadNotificationsByUserId(userId, pageable);
        return ResponseEntity.ok(page);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable String id) {
        try {
            notificationService.deleteNotification(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteNotifications(@RequestBody List<String> ids) {
        try {
            notificationService.deleteNotifications(ids);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/read")
    public ResponseEntity<Void> markNotificationsAsRead(@RequestBody List<String> ids) {
        try {
            notificationService.markNotificationsAsRead(ids);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/user/{userId}/read-all")
    public ResponseEntity<Void> markAllNotificationsAsRead(@PathVariable String userId) {
        try {
            notificationService.markAllNotificationsAsRead(userId);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
