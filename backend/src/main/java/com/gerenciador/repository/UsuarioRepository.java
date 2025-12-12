package com.gerenciador.repository;

import java.util.Optional;

import com.gerenciador.entity.Usuario;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.UUID;

@ApplicationScoped
public class UsuarioRepository implements PanacheRepositoryBase<Usuario, UUID> {

    public Optional<Usuario> findByEmail(String email) {
        return find("email", email).firstResultOptional();
    }
}