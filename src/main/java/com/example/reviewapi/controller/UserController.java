package com.example.reviewapi.controller;

import com.example.reviewapi.dto.req.PasswordChangeRequest;
import com.example.reviewapi.dto.req.UserUpdateRequest;
import com.example.reviewapi.dto.res.UserResponse;
import com.example.reviewapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyProfile(@AuthenticationPrincipal Long userId) {
        UserResponse res = userService.getUserProfile(userId);

        return ResponseEntity.ok(res);
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateMyProfile(
            @AuthenticationPrincipal Long userId,
            @RequestBody UserUpdateRequest req) {
        UserResponse res = userService.updateProfile(userId, req);

        return ResponseEntity.ok(res);
    }

    @PutMapping("/me/password")
    public ResponseEntity<Void> changePassword(
            @AuthenticationPrincipal Long userId,
            @RequestBody PasswordChangeRequest req) {
        userService.changePassword(userId, req);

        // HTTP 204
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserProfile(@PathVariable Long userId) {
        UserResponse res = userService.getUserProfile(userId);
        return ResponseEntity.ok(res);
    }
}
