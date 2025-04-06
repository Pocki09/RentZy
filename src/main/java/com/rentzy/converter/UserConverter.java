package com.rentzy.converter;

import com.rentzy.model.dto.UserDTO;
import com.rentzy.model.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserConverter {
    UserDTO toDTO(UserEntity user);
    UserEntity toEntity(UserDTO userDTO);
}
