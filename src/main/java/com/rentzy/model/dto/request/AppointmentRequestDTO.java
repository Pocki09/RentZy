package com.rentzy.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class AppointmentRequestDTO {
    private String appointmentId;

    @NotBlank(message = "Post ID is required")
    private String postId;

    @NotBlank(message = "User ID is required")
    private String userId;

    @NotNull(message = "Date is required")
    private Date appointmentDate;

    private String notes;

    private Integer durationMinutes;

    private String contactPhone;
}
