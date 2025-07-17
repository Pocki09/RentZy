package com.rentzy.converter;

import com.rentzy.entity.ReviewEntity;
import com.rentzy.model.dto.ReviewDTO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-16T22:28:33+0700",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.50.v20250628-1110, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class ReviewConverterImpl implements ReviewConverter {

    @Override
    public void updateReviewFromDto(ReviewDTO reviewDTO, ReviewEntity reviewEntity) {
        if ( reviewDTO == null ) {
            return;
        }

        reviewEntity.setComment( reviewDTO.getComment() );
        reviewEntity.setId( reviewDTO.getId() );
        reviewEntity.setPostId( reviewDTO.getPostId() );
        reviewEntity.setRating( reviewDTO.getRating() );
        reviewEntity.setUserId( reviewDTO.getUserId() );
    }

    @Override
    public ReviewDTO toDTO(ReviewEntity reviewEntity) {
        if ( reviewEntity == null ) {
            return null;
        }

        ReviewDTO reviewDTO = new ReviewDTO();

        reviewDTO.setComment( reviewEntity.getComment() );
        reviewDTO.setId( reviewEntity.getId() );
        reviewDTO.setPostId( reviewEntity.getPostId() );
        reviewDTO.setRating( reviewEntity.getRating() );
        reviewDTO.setUserId( reviewEntity.getUserId() );

        return reviewDTO;
    }

    @Override
    public ReviewEntity toEntity(ReviewDTO reviewDTO) {
        if ( reviewDTO == null ) {
            return null;
        }

        ReviewEntity reviewEntity = new ReviewEntity();

        reviewEntity.setComment( reviewDTO.getComment() );
        reviewEntity.setId( reviewDTO.getId() );
        reviewEntity.setPostId( reviewDTO.getPostId() );
        reviewEntity.setRating( reviewDTO.getRating() );
        reviewEntity.setUserId( reviewDTO.getUserId() );

        return reviewEntity;
    }
}
