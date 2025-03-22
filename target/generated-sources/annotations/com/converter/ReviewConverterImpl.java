package com.converter;

import com.model.dto.ReviewDTO;
import com.model.entity.ReviewEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-21T17:06:58+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.11 (Oracle Corporation)"
)
@Component
public class ReviewConverterImpl implements ReviewConverter {

    @Override
    public void updateReviewFromDto(ReviewDTO reviewDTO, ReviewEntity reviewEntity) {
        if ( reviewDTO == null ) {
            return;
        }
    }

    @Override
    public ReviewDTO toDTO(ReviewEntity reviewEntity) {
        if ( reviewEntity == null ) {
            return null;
        }

        ReviewDTO reviewDTO = new ReviewDTO();

        return reviewDTO;
    }

    @Override
    public ReviewEntity toEntity(ReviewDTO reviewDTO) {
        if ( reviewDTO == null ) {
            return null;
        }

        ReviewEntity reviewEntity = new ReviewEntity();

        return reviewEntity;
    }
}
