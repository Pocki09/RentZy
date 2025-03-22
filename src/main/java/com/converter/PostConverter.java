package com.converter;

import com.model.dto.PostDTO;
import com.model.entity.PostEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface PostConverter {
    PostDTO toDTO(PostEntity postEntity);
    PostEntity toEntity(PostDTO postDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(PostDTO postDTO, @MappingTarget PostEntity postEntity);
}
