package com.gerenciador.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.gerenciador.dto.DashboardResponse;
import com.gerenciador.entity.TipoTransacao;
import com.gerenciador.entity.Transacao;
import com.gerenciador.repository.TransacaoRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DashboardService {

    @Inject
    TransacaoRepository transacaoRepository;

    public DashboardResponse calcularResumo(UUID usuarioId) {
        List<Transacao> transacoes = transacaoRepository.findByUsuarioId(usuarioId);

        BigDecimal totalReceitas = transacoes.stream()
                .filter(t -> t.getTipo() == TipoTransacao.RECEITA)
                .map(Transacao::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalDespesas = transacoes.stream()
                .filter(t -> t.getTipo() == TipoTransacao.DESPESA)
                .map(Transacao::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal saldo = totalReceitas.subtract(totalDespesas);

        return new DashboardResponse(totalReceitas, totalDespesas, saldo);
    }
}






