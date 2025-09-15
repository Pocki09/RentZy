package com.rentzy.converter;

import com.rentzy.model.dto.request.ReviewRequestDTO;
import com.rentzy.entity.ReviewEntity;
import com.rentzy.model.dto.response.ReviewResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ReviewConverter {
    ReviewResponseDTO toDTO(ReviewEntity reviewEntity);
    ReviewEntity toEntity(ReviewRequestDTO reviewRequestDTO);
}
