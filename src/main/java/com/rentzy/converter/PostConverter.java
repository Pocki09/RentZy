package com.rentzy.converter;

import com.rentzy.model.dto.request.PostRequestDTO;
import com.rentzy.entity.PostEntity;
import com.rentzy.model.dto.response.PostResponseDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PostConverter {
    PostResponseDTO toDTO(PostEntity postEntity);
    PostEntity toEntity(PostRequestDTO postRequestDTO);
}
