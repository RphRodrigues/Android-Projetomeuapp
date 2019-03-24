package com.rtstudio.projetomeuapp.classes;

public class Cliente {
    private int clienteId;
    private String nome;
    private String cpf;
    private String codigoCliente;

    public Cliente(String nome, String cpf, String codigoCliente) {
        this.nome = nome;
        this.cpf = cpf;
        this.codigoCliente = codigoCliente;
    }

    public int getClienteId() {
        return clienteId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(String codigoCliente) {
        this.codigoCliente = codigoCliente;
    }
}
