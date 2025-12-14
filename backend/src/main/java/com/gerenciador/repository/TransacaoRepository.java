package com.gerenciador.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.gerenciador.entity.Transacao;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TransacaoRepository implements PanacheRepositoryBase<Transacao, UUID> {

    public List<Transacao> findByUsuarioId(UUID usuarioId) {
        return find("usuario.id", usuarioId).list();
    }

    public Optional<Transacao> findByIdAndUsuarioId(UUID id, UUID usuarioId) {
        return find("id = ?1 and usuario.id = ?2", id, usuarioId).firstResultOptional();
    }
}
