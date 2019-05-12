package com.rtstudio.projetomeuapp.classes;

import java.io.Serializable;

public class Cliente implements Serializable {
    private int clienteId;
    private String nome;
    private String codigoCliente;

    public Cliente() {
    }

    public Cliente(String nome, String codigoCliente) {
        this.nome = nome;
        this.codigoCliente = codigoCliente;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
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
