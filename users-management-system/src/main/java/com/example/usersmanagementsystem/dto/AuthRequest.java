package com.example.usersmanagementsystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
    @NotBlank(message = "username shouldn't be null")
    @Email(message = "invalid email address")
    private String email;
    @NotBlank(message = "password shouldn't be null")
    private String password;
}
