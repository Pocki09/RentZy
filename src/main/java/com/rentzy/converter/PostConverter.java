package com.rentzy.converter;

import com.rentzy.model.dto.PostDTO;
import com.rentzy.model.entity.PostEntity;
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
