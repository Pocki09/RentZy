package com.rentzy.converter;

import com.rentzy.entity.AppointmentEntity;
import com.rentzy.model.dto.response.AppointmentResponseDTO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-16T23:54:09+0700",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.50.v20250628-1110, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class AppointmentConverterImpl implements AppointmentConverter {

    @Override
    public AppointmentResponseDTO toDTO(AppointmentEntity appointmentEntity) {
        if ( appointmentEntity == null ) {
            return null;
        }

        AppointmentResponseDTO appointmentResponseDTO = new AppointmentResponseDTO();

        appointmentResponseDTO.setAppointmentDate( appointmentEntity.getAppointmentDate() );
        appointmentResponseDTO.setCancelledAt( appointmentEntity.getCancelledAt() );
        appointmentResponseDTO.setCompletedAt( appointmentEntity.getCompletedAt() );
        appointmentResponseDTO.setConfirmedAt( appointmentEntity.getConfirmedAt() );
        appointmentResponseDTO.setContactPhone( appointmentEntity.getContactPhone() );
        appointmentResponseDTO.setCreatedAt( appointmentEntity.getCreatedAt() );
        appointmentResponseDTO.setDurationMinutes( appointmentEntity.getDurationMinutes() );
        appointmentResponseDTO.setPostId( appointmentEntity.getPostId() );
        appointmentResponseDTO.setStatus( appointmentEntity.getStatus() );
        appointmentResponseDTO.setUpdatedAt( appointmentEntity.getUpdatedAt() );
        appointmentResponseDTO.setUserId( appointmentEntity.getUserId() );

        return appointmentResponseDTO;
    }

    @Override
    public AppointmentEntity toEntity(AppointmentResponseDTO appointmentResponseDTO) {
        if ( appointmentResponseDTO == null ) {
            return null;
        }

        AppointmentEntity appointmentEntity = new AppointmentEntity();

        appointmentEntity.setAppointmentDate( appointmentResponseDTO.getAppointmentDate() );
        appointmentEntity.setCancelledAt( appointmentResponseDTO.getCancelledAt() );
        appointmentEntity.setCompletedAt( appointmentResponseDTO.getCompletedAt() );
        appointmentEntity.setConfirmedAt( appointmentResponseDTO.getConfirmedAt() );
        appointmentEntity.setContactPhone( appointmentResponseDTO.getContactPhone() );
        appointmentEntity.setCreatedAt( appointmentResponseDTO.getCreatedAt() );
        appointmentEntity.setDurationMinutes( appointmentResponseDTO.getDurationMinutes() );
        appointmentEntity.setPostId( appointmentResponseDTO.getPostId() );
        appointmentEntity.setStatus( appointmentResponseDTO.getStatus() );
        appointmentEntity.setUpdatedAt( appointmentResponseDTO.getUpdatedAt() );
        appointmentEntity.setUserId( appointmentResponseDTO.getUserId() );

        return appointmentEntity;
    }
}
