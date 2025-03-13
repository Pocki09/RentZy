package com.model.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "user")
public class UserEntity {
    @Id
    private String id;
    private String username;
    private String password;
    private String email;
    private String role; // user, owner, admin
    private String fullName;
    private String phone;
    private String avatar;
    private Date createdAt;
    private Date updatedAt;
}
