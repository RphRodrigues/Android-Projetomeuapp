package com.rtstudio.projetomeuapp.classes;

public class OrdemServico {
    private int ordemServicoId;
    private Cliente cliente;
    private String descricaoServico;
    private Endereco endereco;

    public OrdemServico(Cliente cliente, Endereco endereco, String descricaoServico) {
        this.cliente = cliente;
        this.descricaoServico = descricaoServico;
        this.endereco = endereco;
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
