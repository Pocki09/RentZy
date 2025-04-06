package com.rentzy.converter;

import com.rentzy.model.dto.AppointmentDTO;
import com.rentzy.model.entity.AppointmentEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppointmentConverter {
    AppointmentDTO toDTO(AppointmentEntity appointmentEntity);
    AppointmentEntity toEntity(AppointmentDTO appointmentDTO);
}
