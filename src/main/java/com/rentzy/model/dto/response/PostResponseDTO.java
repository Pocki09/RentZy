package com.rentzy.model.dto.response;

import com.rentzy.enums.PostStatus;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PostResponseDTO {
    private String id;
    private String propertyName;
    private String title;
    private String description;
    private double price;
    private double area;
    private String address;
    private List<String> images;
    private List<String> utilities;
    private String createdBy;
    private PostStatus status;
    private Date createdAt;
    private Date updatedAt;
}
