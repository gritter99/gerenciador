package com.gerenciador.security.jwt;

import java.util.UUID;

import io.smallrye.jwt.build.Jwt;

public class GenerateToken {
    private static Integer JWT_EXPIRATION_TIME = 720 * 5;

    public static String generateToken(String email, UUID userId) {
        return Jwt
                .issuer("https://gerenciadorpj.com")
                .upn(email)
                .groups("user")
                .claim("uuid", userId.toString())
                .expiresIn(JWT_EXPIRATION_TIME)
                .sign();
    }
}