package com.rentzy.model.dto;

import com.rentzy.enums.PostStatus;
import lombok.Data;

import java.util.Set;

@Data
public class PostSearchDTO {
    private String title;
    private String address;
    private Double minPrice;
    private Double maxPrice;
    private Set<String> utilities;
    private PostStatus status;
}
