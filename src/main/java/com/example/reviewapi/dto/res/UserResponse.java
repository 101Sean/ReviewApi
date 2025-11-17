package com.example.reviewapi.dto.res;

import com.example.reviewapi.domain.Role;
import com.example.reviewapi.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserResponse {
    private final Long userId;
    private final String username;
    private final String nickname;
    private final String profileImageUrl;
    private final String bio;
    private final Role role;
    private final LocalDateTime createdAt;

    // Entity를 DTO로 변환
    public static UserResponse fromEntity(User user) {
        return UserResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .profileImageUrl(user.getProfileImageUrl())
                .bio(user.getBio())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
