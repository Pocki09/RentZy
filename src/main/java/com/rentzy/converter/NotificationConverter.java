package com.rentzy.converter;

import com.rentzy.entity.NotificationEntity;
import com.rentzy.model.dto.request.NotificationRequestDTO;
import com.rentzy.model.dto.response.NotificationResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationConverter {
    NotificationResponseDTO toDTO(NotificationEntity notification);
    NotificationEntity toEntity(NotificationRequestDTO dto);
}
