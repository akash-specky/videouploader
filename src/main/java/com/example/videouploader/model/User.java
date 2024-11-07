package com.example.videouploader.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Document(collection = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private String userId;
    private String name;
    private String occupation;

    @Indexed(unique = true)
    private String email;

    @Indexed(unique = true)
    private String mobileNo;

    private String address;
    private String tokenValue;
    private String country;
    private String pincode;
    private String password;
    private String  ssoId ;
    private String  pictureUrl ;
    private String  family_name ;
    private Date createdAt;
    private Date updatedAt;
    private String  given_name ;
    private boolean isAdmin ;
    private LocalDateTime lastActiveAt;


}

