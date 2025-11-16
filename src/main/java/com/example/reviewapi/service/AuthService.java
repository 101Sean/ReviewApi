package com.example.reviewapi.service;

import com.example.reviewapi.dto.req.LoginRequest;
import com.example.reviewapi.dto.req.RegisterRequest;
import com.example.reviewapi.dto.res.TokenResponse;
import com.example.reviewapi.entity.User;
import com.example.reviewapi.repository.UserRepository;
import com.example.reviewapi.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    private static final String REFRESH_TOKEN_PREFIX = "RT:";

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

    @Transactional
    public TokenResponse login(LoginRequest req) {
        User user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new BadCredentialsException("사용자를 찾을 수 없습니다."));

        if(!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        Long userId = user.getId();
        var authorities = user.getAuthorities();

        String accessToken = jwtTokenProvider.generateAccessToken(userId, authorities);
        String refreshToken = jwtTokenProvider.generateRefreshToken(userId, authorities);
        long refreshTokenExpiry = jwtTokenProvider.getRefreshTokenExpiryTime();

        redisTemplate.opsForValue().set(
                REFRESH_TOKEN_PREFIX + user.getId(),
                refreshToken,
                refreshTokenExpiry,
                TimeUnit.MILLISECONDS
        );

        return new TokenResponse(accessToken, refreshToken);
    }

    @Transactional
    public void logout(Long userId) {
        String key = REFRESH_TOKEN_PREFIX + userId;
        if(redisTemplate.hasKey(key)) {
            redisTemplate.delete(key);
        }
    }

    // AccessToken 갱신 (with RefreshToken)
    @Transactional
    public TokenResponse refreshAccessToken(String token) {
        if(!jwtTokenProvider.validateToken(token)) {
            throw new BadCredentialsException("유효하지 않거나 만료된 Refresh Token 입니다.");
        }

        Long userId = jwtTokenProvider.getUserIdFromToken(token);

        String storedRefreshToken = redisTemplate.opsForValue().get(REFRESH_TOKEN_PREFIX + userId);

        if(storedRefreshToken == null || storedRefreshToken.equals(token)) {
            throw new BadCredentialsException("Refresh Token이 Redis에 저장된 값과 일치하지 않습니다. (재로그인 필요)");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadCredentialsException("사용자를 찾을 수 없습니다."));

        String newAccessToken = jwtTokenProvider.generateAccessToken(userId, user.getAuthorities());

        return new TokenResponse(newAccessToken, token);
    }


}
