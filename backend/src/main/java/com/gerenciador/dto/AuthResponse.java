package com.gerenciador.dto;

import java.util.UUID;

public class AuthResponse {

    private String token;
    private UsuarioResponse usuario;

    public AuthResponse(String token, UsuarioResponse usuario) {
        this.token = token;
        this.usuario = usuario;
    }

    public String getToken() {
        return token;
    }

    public UsuarioResponse getUsuario() {
        return usuario;
    }

    public static class UsuarioResponse {
        private UUID id;
        private String nome;
        private String email;

        public UsuarioResponse(UUID id, String nome, String email) {
            this.id = id;
            this.nome = nome;
            this.email = email;
        }

        public UUID getId() {
            return id;
        }

        public String getNome() {
            return nome;
        }

        public String getEmail() {
            return email;
        }
    }
}