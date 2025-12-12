package com.gerenciador.service;

import org.mindrot.jbcrypt.BCrypt;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PasswordService {
    public String hash(String senha) {
        return BCrypt.hashpw(senha, BCrypt.gensalt());
    }

    public boolean verify(String senha, String hash) {
        return BCrypt.checkpw(senha, hash);
    }
}
