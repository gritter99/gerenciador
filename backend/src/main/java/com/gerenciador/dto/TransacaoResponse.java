package com.gerenciador.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.gerenciador.entity.TipoTransacao;
import com.gerenciador.entity.Transacao;

public class TransacaoResponse {

    private UUID id;
    private TipoTransacao tipo;
    private LocalDate data;
    private BigDecimal valor;
    private String descricao;

    public TransacaoResponse() {
    }

    public TransacaoResponse(Transacao transacao) {
        this.id = transacao.getId();
        this.tipo = transacao.getTipo();
        this.data = transacao.getData();
        this.valor = transacao.getValor();
        this.descricao = transacao.getDescricao();
    }

    // Getters e Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public TipoTransacao getTipo() {
        return tipo;
    }

    public void setTipo(TipoTransacao tipo) {
        this.tipo = tipo;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
