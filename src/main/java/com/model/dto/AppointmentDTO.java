package com.model.dto;

import com.enums.AppointmentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class AppointmentDTO {
    @NotBlank(message = "Post ID is required")
    private String postId;

    @NotBlank(message = "User ID is required")
    private String userId;

    @NotBlank(message = "Owner ID is required")
    private String ownerId;

    @NotNull(message = "Start Date is required")
    private Date startDate;

    @NotNull(message = "End Date is required")
    private Date endDate;

    private AppointmentStatus status;
}
