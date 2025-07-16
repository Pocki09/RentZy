package com.rentzy.model.dto.response;

import com.rentzy.enums.AppointmentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class AppointmentResponseDTO {
    private String appointmentId;

    private String postId;

  
    private String userId;

    private Date appointmentDate;

    private AppointmentStatus status;

    private String note;

    private Integer durationMinutes;

    private String contactPhone;

    private Date createdAt;

    private Date updatedAt;

}
