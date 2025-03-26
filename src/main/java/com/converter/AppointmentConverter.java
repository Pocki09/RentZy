package com.converter;

import com.model.dto.AppointmentDTO;
import com.model.entity.AppointmentEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppointmentConverter {
    AppointmentDTO toDTO(AppointmentEntity appointmentEntity);
    AppointmentEntity toEntity(AppointmentDTO appointmentDTO);
}
