package com.gerenciador.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.gerenciador.dto.TransacaoRequest;
import com.gerenciador.dto.TransacaoResponse;
import com.gerenciador.entity.Transacao;
import com.gerenciador.entity.Usuario;
import com.gerenciador.exception.CustomException;
import com.gerenciador.repository.TransacaoRepository;
import com.gerenciador.repository.UsuarioRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class TransacaoService {

    @Inject
    TransacaoRepository transacaoRepository;

    @Inject
    UsuarioRepository usuarioRepository;

    @Transactional
    public TransacaoResponse criar(TransacaoRequest request, UUID usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId);
        if (usuario == null) {
            throw new CustomException("Usuário não encontrado");
        }

        Transacao transacao = new Transacao();
        transacao.setTipo(request.getTipo());
        transacao.setData(request.getData());
        transacao.setValor(request.getValor());
        transacao.setDescricao(request.getDescricao());
        transacao.setUsuario(usuario);

        transacaoRepository.persist(transacao);

        return new TransacaoResponse(transacao);
    }

    public List<TransacaoResponse> listar(UUID usuarioId) {
        return transacaoRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(TransacaoResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void excluir(UUID id, UUID usuarioId) {
        Transacao transacao = transacaoRepository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new CustomException("Transação não encontrada"));

        transacaoRepository.delete(transacao);
    }
}
