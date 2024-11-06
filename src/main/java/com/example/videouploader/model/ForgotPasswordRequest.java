package com.example.videouploader.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForgotPasswordRequest {

    private String userId;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;

}
