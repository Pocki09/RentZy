package com.converter;

import com.model.dto.NotificationDTO;
import com.model.entity.NotificationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationConverter {
    NotificationDTO toDTO(NotificationEntity notification);
    NotificationEntity toEntity(NotificationDTO dto);
}
