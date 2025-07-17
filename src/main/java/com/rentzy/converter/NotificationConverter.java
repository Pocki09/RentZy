package com.rentzy.converter;

import com.rentzy.model.dto.NotificationDTO;
import com.rentzy.entity.NotificationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationConverter {
    NotificationDTO toDTO(NotificationEntity notification);
    NotificationEntity toEntity(NotificationDTO dto);
}
