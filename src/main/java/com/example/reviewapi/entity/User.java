package com.example.reviewapi.entity;

import com.example.reviewapi.config.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "user")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String username;  // ID

    @Column(nullable = false, length = 255)
    private String password;

    @Column(unique = true, nullable = false, length = 50)
    private String nickname;

    @Column(length = 500)
    private String profileImageUrl;

    @Column(length = 255)
    private String bio;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private Role role;
}
