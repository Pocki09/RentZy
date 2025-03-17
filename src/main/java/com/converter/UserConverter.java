package com.converter;

import com.model.dto.UserDTO;
import com.model.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserConverter {
    UserDTO toDTO(UserEntity user);
    UserEntity toEntity(UserDTO userDTO);
}
