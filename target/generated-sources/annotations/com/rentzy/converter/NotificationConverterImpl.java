package com.rentzy.converter;

import com.rentzy.entity.NotificationEntity;
import com.rentzy.enums.NotificationType;
import com.rentzy.model.dto.NotificationDTO;
import java.util.Date;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-16T22:28:33+0700",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.50.v20250628-1110, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class NotificationConverterImpl implements NotificationConverter {

    @Override
    public NotificationDTO toDTO(NotificationEntity notification) {
        if ( notification == null ) {
            return null;
        }

        NotificationDTO notificationDTO = new NotificationDTO();

        if ( notification.getCreatedAt() != null ) {
            notificationDTO.setCreatedAt( notification.getCreatedAt().toInstant() );
        }
        notificationDTO.setMessage( notification.getMessage() );
        if ( notification.getType() != null ) {
            notificationDTO.setType( notification.getType().name() );
        }
        notificationDTO.setUserId( notification.getUserId() );

        return notificationDTO;
    }

    @Override
    public NotificationEntity toEntity(NotificationDTO dto) {
        if ( dto == null ) {
            return null;
        }

        NotificationEntity notificationEntity = new NotificationEntity();

        if ( dto.getCreatedAt() != null ) {
            notificationEntity.setCreatedAt( Date.from( dto.getCreatedAt() ) );
        }
        notificationEntity.setMessage( dto.getMessage() );
        if ( dto.getType() != null ) {
            notificationEntity.setType( Enum.valueOf( NotificationType.class, dto.getType() ) );
        }
        notificationEntity.setUserId( dto.getUserId() );

        return notificationEntity;
    }
}
