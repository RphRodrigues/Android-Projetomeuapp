package com.rtstudio.projetomeuapp.classes;

import java.util.Random;

public class OrdemServico {
    private int ordemServicoId;
    private Cliente cliente;
    private String descricaoServico;
    private Endereco endereco;

    public OrdemServico(Cliente cliente, Endereco endereco, String descricaoServico) {
        this.ordemServicoId = gerarId();
        this.cliente = cliente;
        this.descricaoServico = descricaoServico;
        this.endereco = endereco;
    }

    private int gerarId() {
        return new Random().nextInt(1000) + 100;
    }

    public int getOrdemServicoId() {
        return ordemServicoId;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getDescricaoServico() {
        return descricaoServico;
    }

    public void setDescricaoServico(String descricaoServico) {
        this.descricaoServico = descricaoServico;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }
}
