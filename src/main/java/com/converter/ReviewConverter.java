package com.converter;

import com.model.dto.ReviewDTO;
import com.model.entity.ReviewEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ReviewConverter {
     // Không cập nhật ID
    @Mapping(target = "id", ignore = true)
    void updateReviewFromDto(ReviewDTO reviewDTO, @MappingTarget ReviewEntity reviewEntity);

    ReviewDTO toDTO(ReviewEntity reviewEntity);
    ReviewEntity toEntity(ReviewDTO reviewDTO);
}
