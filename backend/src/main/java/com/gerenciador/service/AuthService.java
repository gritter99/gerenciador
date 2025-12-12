package com.gerenciador.service;

import com.gerenciador.dto.LoginRequest;
import com.gerenciador.dto.LoginResponse;
import com.gerenciador.entity.Usuario;
import com.gerenciador.exception.CustomException;
import com.gerenciador.security.jwt.GenerateToken;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AuthService {

    @Inject
    PasswordService passwordService;

    private static Integer JWT_EXPIRATION_TIME = 720;

    public LoginResponse generateToken(LoginRequest loginRequest) {
        var user = (Usuario) Usuario.find("email", loginRequest.getEmail())
                .firstResultOptional()
                .orElseThrow(() -> new CustomException("Falha na autenticação, email ou senha incorretos"));

        if (!passwordService.verify(loginRequest.getSenha(), user.getSenhaHash())) {
            throw new CustomException("Falha na autenticação, email ou senha incorretos");
        }

        var jwt = GenerateToken.generateToken(loginRequest.getEmail(), user.getId());
        return new LoginResponse(jwt, JWT_EXPIRATION_TIME);
    }
}
