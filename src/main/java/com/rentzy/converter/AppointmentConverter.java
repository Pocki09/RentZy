package com.rentzy.converter;

import com.rentzy.model.dto.request.AppointmentRequestDTO;
import com.rentzy.model.dto.response.AppointmentResponseDTO;
import com.rentzy.entity.AppointmentEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppointmentConverter {
    AppointmentResponseDTO toDTO(AppointmentEntity appointmentEntity);
    AppointmentEntity toEntity(AppointmentRequestDTO appointmentRequestDTO);
}
