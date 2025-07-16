package com.rentzy.converter;

import com.rentzy.entity.UserEntity;
import com.rentzy.model.dto.request.UserRequestDTO;
import com.rentzy.model.dto.response.UserResponseDTO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-16T22:28:33+0700",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.50.v20250628-1110, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class UserConverterImpl implements UserConverter {

    @Override
    public UserResponseDTO toDTO(UserEntity user) {
        if ( user == null ) {
            return null;
        }

        UserResponseDTO userResponseDTO = new UserResponseDTO();

        userResponseDTO.setAvatar( user.getAvatar() );
        userResponseDTO.setDob( user.getDob() );
        userResponseDTO.setEmail( user.getEmail() );
        userResponseDTO.setFullName( user.getFullName() );
        userResponseDTO.setId( user.getId() );
        userResponseDTO.setPhone( user.getPhone() );
        userResponseDTO.setRole( user.getRole() );
        userResponseDTO.setUsername( user.getUsername() );

        return userResponseDTO;
    }

    @Override
    public UserEntity toEntity(UserRequestDTO userRequestDTO) {
        if ( userRequestDTO == null ) {
            return null;
        }

        UserEntity.UserEntityBuilder userEntity = UserEntity.builder();

        userEntity.avatar( userRequestDTO.getAvatar() );
        userEntity.dob( userRequestDTO.getDob() );
        userEntity.email( userRequestDTO.getEmail() );
        userEntity.fullName( userRequestDTO.getFullName() );
        userEntity.password( userRequestDTO.getPassword() );
        userEntity.phone( userRequestDTO.getPhone() );
        userEntity.role( userRequestDTO.getRole() );
        userEntity.username( userRequestDTO.getUsername() );

        return userEntity.build();
    }
}
