package com.rentzy.converter;

import com.rentzy.model.dto.ReviewDTO;
import com.rentzy.model.entity.ReviewEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ReviewConverter {

    void updateReviewFromDto(ReviewDTO reviewDTO, @MappingTarget ReviewEntity reviewEntity);

    ReviewDTO toDTO(ReviewEntity reviewEntity);
    ReviewEntity toEntity(ReviewDTO reviewDTO);
}
