package com.converter;

import com.model.dto.AppointmentDTO;
import com.model.entity.AppointmentEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-27T16:09:32+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.11 (Oracle Corporation)"
)
@Component
public class AppointmentConverterImpl implements AppointmentConverter {

    @Override
    public AppointmentDTO toDTO(AppointmentEntity appointmentEntity) {
        if ( appointmentEntity == null ) {
            return null;
        }

        AppointmentDTO appointmentDTO = new AppointmentDTO();

        return appointmentDTO;
    }

    @Override
    public AppointmentEntity toEntity(AppointmentDTO appointmentDTO) {
        if ( appointmentDTO == null ) {
            return null;
        }

        AppointmentEntity appointmentEntity = new AppointmentEntity();

        return appointmentEntity;
    }
}
