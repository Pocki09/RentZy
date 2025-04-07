package com.converter;

import com.model.dto.NotificationDTO;
import com.model.entity.NotificationEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-27T16:09:32+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.11 (Oracle Corporation)"
)
@Component
public class NotificationConverterImpl implements NotificationConverter {

    @Override
    public NotificationDTO toDTO(NotificationEntity notification) {
        if ( notification == null ) {
            return null;
        }

        NotificationDTO notificationDTO = new NotificationDTO();

        return notificationDTO;
    }

    @Override
    public NotificationEntity toEntity(NotificationDTO dto) {
        if ( dto == null ) {
            return null;
        }

        NotificationEntity notificationEntity = new NotificationEntity();

        return notificationEntity;
    }
}
