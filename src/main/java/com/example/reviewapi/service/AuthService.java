package com.example.reviewapi.service;

import com.example.reviewapi.dto.req.RegisterRequest;
import com.example.reviewapi.entity.User;
import com.example.reviewapi.repository.UserRepository;
import com.example.reviewapi.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    // Register
    public User register(RegisterRequest req) {
        if(userRepository.findByUsername(req.getUsername()).isPresent()) {
            throw new IllegalArgumentException("이미 등록된 사용자 입니다.");
        }

        String encodedPassword = passwordEncoder.encode(req.getPassword());

        User newUser = User.builder()
                .username(req.getUsername())
                .password(encodedPassword)
                .nickname(req.getNickname())
                .role(req.getRole())
                .build();

        return userRepository.save(newUser);
    }

    // AccessToken 갱신 (with RefreshToken)
    public String refreshAccessToken(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new IllegalArgumentException("유효하지 않거나 만료된 Refresh Token 입니다.");
        }
        // TODO : Redis 사용시 검증단계 구현

    }
}
