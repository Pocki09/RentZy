package com.converter;


import javax.annotation.processing.Generated;

import com.rentzy.converter.UserConverter;
import com.rentzy.model.dto.request.UserRequestDTO;
import com.rentzy.model.dto.response.UserResponseDTO;
import com.rentzy.entity.UserEntity;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-27T16:09:32+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.11 (Oracle Corporation)"
)
@Component
public class UserConverterImpl implements UserConverter {


    @Override
    public UserResponseDTO toDTO(UserEntity user) {
        return null;
    }

    @Override
    public UserEntity toEntity(UserRequestDTO userRequestDTO) {
        return null;
    }
}
