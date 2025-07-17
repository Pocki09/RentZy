package com.rentzy.converter;

import com.rentzy.model.dto.request.UserRequestDTO;
import com.rentzy.model.dto.response.UserResponseDTO;
import com.rentzy.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserConverter {
    UserResponseDTO toDTO(UserEntity user);
    UserEntity toEntity(UserRequestDTO userRequestDTO);
}
