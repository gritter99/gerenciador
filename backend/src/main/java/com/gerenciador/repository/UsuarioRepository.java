package com.gerenciador.repository;

import java.util.Optional;

import com.gerenciador.entity.Usuario;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.UUID;

@ApplicationScoped
public class UsuarioRepository implements PanacheRepositoryBase<Usuario, UUID> {

    public Usuario findById(UUID id) {
        return find("id", id).firstResult();
    }

    public Optional<Usuario> findByEmail(String email) {
        return find("email", email).firstResultOptional();
    }
}