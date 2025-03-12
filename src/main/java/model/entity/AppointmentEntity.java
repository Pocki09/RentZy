package model.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "appointment")
public class AppointmentEntity {
    @Id
    private String id;
    private String postId; // Post ID
    private String userId; // User ID (renter)
    private String ownerId; // User ID (owner)
    private Date date;
    private String status; // pending, confirmed, canceled
    private Date createdAt;
    private Date updatedAt;
}
