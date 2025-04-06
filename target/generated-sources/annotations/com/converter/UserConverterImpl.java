package com.converter;

import com.model.dto.UserDTO;
import com.model.entity.UserEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-27T16:09:32+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.11 (Oracle Corporation)"
)
@Component
public class UserConverterImpl implements UserConverter {

    @Override
    public UserDTO toDTO(UserEntity user) {
        if ( user == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        return userDTO;
    }

    @Override
    public UserEntity toEntity(UserDTO userDTO) {
        if ( userDTO == null ) {
            return null;
        }

        UserEntity userEntity = new UserEntity();

        return userEntity;
    }
}
