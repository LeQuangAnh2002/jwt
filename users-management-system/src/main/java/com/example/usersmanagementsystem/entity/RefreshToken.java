package com.example.usersmanagementsystem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "refresh_token")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String token;

    private Instant expiryDate;
    @OneToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private OurUsers ourUsers;
}
