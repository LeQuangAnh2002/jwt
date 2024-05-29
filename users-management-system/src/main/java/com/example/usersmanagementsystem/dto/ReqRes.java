package com.example.usersmanagementsystem.dto;

import com.example.usersmanagementsystem.entity.OurUsers;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReqRes {

    private int statusCode;
    private String error;
    private String message;
    private String token;
    private String refreshToken;
    private String expirationTime;
    @NotBlank(message = "username cannot be blank")
    @Size(min = 6,max = 50,message = "Name must be from 6 to 50 characters")
    private String name;
    @NotBlank(message = "city cannot be blank")
    @Size(min = 6,max = 20,message = "City must be from 6 to 20 characters")
    private String city;
    @NotBlank(message = "role cannot be blank")
    private String role;
    @NotBlank(message = "email cannot be blank")
    @Email(message = "invalid email address")
    private String email;
    @NotBlank(message = "password cannot be blank")
    @Size(min = 6,max = 12,message = "Password must be from 6 to 12 characters")
    private String password;
    private String img;
    private OurUsers ourUsers;
    private List<OurUsers> ourUsersList;

}
