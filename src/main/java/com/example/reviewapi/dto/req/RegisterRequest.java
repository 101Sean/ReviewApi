package com.example.reviewapi.dto.req;

import com.example.reviewapi.domain.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String username;
    private String password;
    private String nickname;

    private Role role = Role.USER;
}
