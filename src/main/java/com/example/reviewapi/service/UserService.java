package com.example.reviewapi.service;

import com.example.reviewapi.dto.req.PasswordChangeRequest;
import com.example.reviewapi.dto.req.UserUpdateRequest;
import com.example.reviewapi.dto.res.UserResponse;
import com.example.reviewapi.entity.User;
import com.example.reviewapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다: " + userId));

        return UserResponse.fromEntity(user);
    }

    @Transactional
    public UserResponse updateProfile(Long userId, UserUpdateRequest req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다: " + userId));

        user.updateProfile(req.getNickname(), req.getProfileImageUrl(), req.getBio());

        return UserResponse.fromEntity(user);
    }

    @Transactional
    public void changePassword(Long userId, PasswordChangeRequest req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다: " + userId));

        if(!passwordEncoder.matches(req.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        String encodedNewPassword = passwordEncoder.encode(req.getNewPassword());
        user.updatePassword(encodedNewPassword);
    }
}