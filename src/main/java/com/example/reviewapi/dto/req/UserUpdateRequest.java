package com.example.reviewapi.dto.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {
    private String nickname;
    private String profileImageUrl;
    private String bio;
}
