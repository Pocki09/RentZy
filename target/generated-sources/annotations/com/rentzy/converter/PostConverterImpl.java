package com.rentzy.converter;

import com.rentzy.entity.PostEntity;
import com.rentzy.model.dto.PostDTO;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-16T22:28:33+0700",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.50.v20250628-1110, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class PostConverterImpl implements PostConverter {

    @Override
    public PostDTO toDTO(PostEntity postEntity) {
        if ( postEntity == null ) {
            return null;
        }

        PostDTO postDTO = new PostDTO();

        postDTO.setAddress( postEntity.getAddress() );
        postDTO.setArea( postEntity.getArea() );
        postDTO.setDescription( postEntity.getDescription() );
        postDTO.setId( postEntity.getId() );
        List<String> list = postEntity.getImages();
        if ( list != null ) {
            postDTO.setImages( new ArrayList<String>( list ) );
        }
        postDTO.setPrice( postEntity.getPrice() );
        postDTO.setStatus( postEntity.getStatus() );
        postDTO.setTitle( postEntity.getTitle() );
        List<String> list1 = postEntity.getUtilities();
        if ( list1 != null ) {
            postDTO.setUtilities( new LinkedHashSet<String>( list1 ) );
        }

        return postDTO;
    }

    @Override
    public PostEntity toEntity(PostDTO postDTO) {
        if ( postDTO == null ) {
            return null;
        }

        PostEntity postEntity = new PostEntity();

        postEntity.setAddress( postDTO.getAddress() );
        postEntity.setArea( postDTO.getArea() );
        postEntity.setDescription( postDTO.getDescription() );
        postEntity.setId( postDTO.getId() );
        List<String> list = postDTO.getImages();
        if ( list != null ) {
            postEntity.setImages( new ArrayList<String>( list ) );
        }
        postEntity.setPrice( postDTO.getPrice() );
        postEntity.setStatus( postDTO.getStatus() );
        postEntity.setTitle( postDTO.getTitle() );
        Set<String> set = postDTO.getUtilities();
        if ( set != null ) {
            postEntity.setUtilities( new ArrayList<String>( set ) );
        }

        return postEntity;
    }

    @Override
    public void updateEntityFromDTO(PostDTO postDTO, PostEntity postEntity) {
        if ( postDTO == null ) {
            return;
        }

        if ( postDTO.getAddress() != null ) {
            postEntity.setAddress( postDTO.getAddress() );
        }
        postEntity.setArea( postDTO.getArea() );
        if ( postDTO.getDescription() != null ) {
            postEntity.setDescription( postDTO.getDescription() );
        }
        if ( postDTO.getId() != null ) {
            postEntity.setId( postDTO.getId() );
        }
        if ( postEntity.getImages() != null ) {
            List<String> list = postDTO.getImages();
            if ( list != null ) {
                postEntity.getImages().clear();
                postEntity.getImages().addAll( list );
            }
        }
        else {
            List<String> list = postDTO.getImages();
            if ( list != null ) {
                postEntity.setImages( new ArrayList<String>( list ) );
            }
        }
        postEntity.setPrice( postDTO.getPrice() );
        if ( postDTO.getStatus() != null ) {
            postEntity.setStatus( postDTO.getStatus() );
        }
        if ( postDTO.getTitle() != null ) {
            postEntity.setTitle( postDTO.getTitle() );
        }
        if ( postEntity.getUtilities() != null ) {
            Set<String> set = postDTO.getUtilities();
            if ( set != null ) {
                postEntity.getUtilities().clear();
                postEntity.getUtilities().addAll( set );
            }
        }
        else {
            Set<String> set = postDTO.getUtilities();
            if ( set != null ) {
                postEntity.setUtilities( new ArrayList<String>( set ) );
            }
        }
    }
}
