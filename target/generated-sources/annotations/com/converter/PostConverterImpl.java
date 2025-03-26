package com.converter;

import com.model.dto.PostDTO;
import com.model.entity.PostEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-24T16:28:45+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.11 (Oracle Corporation)"
)
@Component
public class PostConverterImpl implements PostConverter {

    @Override
    public PostDTO toDTO(PostEntity postEntity) {
        if ( postEntity == null ) {
            return null;
        }

        PostDTO postDTO = new PostDTO();

        return postDTO;
    }

    @Override
    public PostEntity toEntity(PostDTO postDTO) {
        if ( postDTO == null ) {
            return null;
        }

        PostEntity postEntity = new PostEntity();

        return postEntity;
    }

    @Override
    public void updateEntityFromDTO(PostDTO postDTO, PostEntity postEntity) {
        if ( postDTO == null ) {
            return;
        }
    }
}
