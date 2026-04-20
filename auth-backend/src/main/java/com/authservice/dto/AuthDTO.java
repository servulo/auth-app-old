package com.authservice.dto;

public class AuthDTO {

    public static class RegisterRequest {
        public String name;
        public String email;
        public String password;
    }

    public static class LoginRequest {
        public String email;
        public String password;
    }

    public static class AuthResponse {
        public String token;
        public Long userId;
        public String name;
        public String email;

        public AuthResponse(String token, Long userId, String name, String email) {
            this.token = token;
            this.userId = userId;
            this.name = name;
            this.email = email;
        }
    }

    public static class UserResponse {
        public Long userId;
        public String name;
        public String email;
        public String createdAt;

        public UserResponse(Long userId, String name, String email, String createdAt) {
            this.userId = userId;
            this.name = name;
            this.email = email;
            this.createdAt = createdAt;
        }
    }

    public static class UpdateProfileRequest {
        public String name;
    }

    public static class UpdatePasswordRequest {
        public String currentPassword;
        public String newPassword;
    }
}