package model.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Document(collection = "post")
public class PostEntity {
    @Id
    private String id;
    private String title;
    private String description;
    private double price;
    private double area;
    private String address;
    private List<String> images;
    private List<String> utilities;
    private String createdBy; // User ID
    private String status; // pending, approved, rejected
    private Date createdAt;
    private Date updatedAt;
}
