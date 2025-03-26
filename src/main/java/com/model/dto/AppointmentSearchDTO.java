package com.model.dto;

import com.enums.AppointmentStatus;
import lombok.Data;

import java.util.Date;

@Data
public class AppointmentSearchDTO {
    private String postId;
    private String userId;
    private String ownerId;
    private Date startDate;
    private Date endDate;
    private AppointmentStatus status;
}
