package com.gerenciador.repository;

import java.util.UUID;

import com.gerenciador.entity.Usuario;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UsuarioRepository implements PanacheRepository<Usuario> {

    public Usuario findById(UUID id) {
        return find("id", id).firstResult();
    }
    public Usuario findByEmail(String email) {
        return find("email", email).firstResult();
    }
}