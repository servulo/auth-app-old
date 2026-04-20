package com.authservice.service;

import com.authservice.dto.AuthDTO;
import com.authservice.entity.User;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotAuthorizedException;

@ApplicationScoped
public class AuthService {

    @Transactional
    public AuthDTO.AuthResponse register(AuthDTO.RegisterRequest request) {
        if (User.findByEmail(request.email) != null) {
            throw new BadRequestException("E-mail já cadastrado");
        }

        User user = new User();
        user.name = request.name;
        user.email = request.email;
        user.passwordHash = BcryptUtil.bcryptHash(request.password);
        user.persist();

        String token = generateToken(user);
        return new AuthDTO.AuthResponse(token, user.id, user.name, user.email);
    }

    public AuthDTO.AuthResponse login(AuthDTO.LoginRequest request) {
        User user = User.findByEmail(request.email);
        if (user == null || !BcryptUtil.matches(request.password, user.passwordHash)) {
            throw new NotAuthorizedException("Credenciais inválidas");
        }

        String token = generateToken(user);
        return new AuthDTO.AuthResponse(token, user.id, user.name, user.email);
    }

    @Transactional
    public void updateProfile(Long userId, AuthDTO.UpdateProfileRequest request) {
        User user = User.findById(userId);
        if (user == null) {
            throw new BadRequestException("Usuário não encontrado");
        }
        user.name = request.name;
    }

    @Transactional
    public void updatePassword(Long userId, AuthDTO.UpdatePasswordRequest request) {
        User user = User.findById(userId);
        if (user == null) {
            throw new BadRequestException("Usuário não encontrado");
        }
        if (!BcryptUtil.matches(request.currentPassword, user.passwordHash)) {
            throw new BadRequestException("Senha atual incorreta");
        }
        user.passwordHash = BcryptUtil.bcryptHash(request.newPassword);
    }

    private String generateToken(User user) {
        return Jwt.issuer("https://authservice.com")
                .subject(String.valueOf(user.id))
                .claim("name", user.name)
                .claim("email", user.email)
                .sign();
    }
}