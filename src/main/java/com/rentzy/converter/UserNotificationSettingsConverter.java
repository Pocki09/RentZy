package com.rentzy.converter;


import com.rentzy.entity.UserNotificationSettingsEntity;
import com.rentzy.model.dto.request.UserNotificationSettingsRequestDTO;
import com.rentzy.model.dto.response.UserNotificationSettingsResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserNotificationSettingsConverter {
    UserNotificationSettingsResponseDTO toDto(UserNotificationSettingsEntity entity);
    UserNotificationSettingsEntity toEntity(UserNotificationSettingsRequestDTO dto);
}
