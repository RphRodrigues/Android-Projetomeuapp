package com.rtstudio.projetomeuapp.classes;

public class OrdemServico {
    private int ordemServicoId;
    private Cliente cliente;
    private String informacaoServico;
    private Endereco endereco;

    public OrdemServico(Cliente cliente, Endereco endereco, String informacaoServico) {
        this.cliente = cliente;
        this.informacaoServico = informacaoServico;
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

    public String getInformacaoServico() {
        return informacaoServico;
    }

    public void setInformacaoServico(String informacaoServico) {
        this.informacaoServico = informacaoServico;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }
}
