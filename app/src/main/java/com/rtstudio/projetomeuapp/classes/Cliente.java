package com.rtstudio.projetomeuapp.classes;

public class Cliente {
    private int clienteId;
    private String nome;
    private String codigoCliente;

    public Cliente(String nome, String codigoCliente) {
        this.nome = nome;
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

    public String getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(String codigoCliente) {
        this.codigoCliente = codigoCliente;
    }
}
