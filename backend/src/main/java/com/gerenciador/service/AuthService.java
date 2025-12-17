package com.gerenciador.service;

import com.gerenciador.dto.AuthResponse;
import com.gerenciador.dto.LoginRequest;
import com.gerenciador.entity.Usuario;
import com.gerenciador.exception.CustomException;
import com.gerenciador.security.jwt.GenerateToken;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AuthService {

    @Inject
    PasswordService passwordService;

    public AuthResponse authenticate(LoginRequest loginRequest) {
        var user = (Usuario) Usuario.find("email", loginRequest.getEmail())
                .firstResultOptional()
                .orElseThrow(() -> new CustomException("Falha na autenticação, email ou senha incorretos"));

        if (!passwordService.verify(loginRequest.getSenha(), user.getSenhaHash())) {
            throw new CustomException("Falha na autenticação, email ou senha incorretos");
        }

        var token = GenerateToken.generateToken(user.getEmail(), user.getId());
        var usuarioResponse = new AuthResponse.UsuarioResponse(
                user.getId(),
                user.getNome(),
                user.getEmail());
        return new AuthResponse(token, usuarioResponse);
    }
}
