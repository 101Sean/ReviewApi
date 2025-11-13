package com.example.reviewapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "restaurant")
public class Restaurant extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 250)
    private String address;

    @Column(precision = 2, scale = 1, nullable = false)
    private BigDecimal averageRating;

    @Column(nullable = false)
    private Integer reviewCount;

    @Column(length = 500)
    private String naverUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "restaurant")
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant")
    private List<RestaurantCategory> categories = new ArrayList<>();
}
