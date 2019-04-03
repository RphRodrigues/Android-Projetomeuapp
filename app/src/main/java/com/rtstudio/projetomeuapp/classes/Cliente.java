package com.rtstudio.projetomeuapp.classes;

import android.os.Parcel;
import android.os.Parcelable;

public class Cliente implements Parcelable {
    private int clienteId;
    private String nome;
    private String codigoCliente;

    public Cliente(String nome, String codigoCliente) {
        this.nome = nome;
        this.codigoCliente = codigoCliente;
    }

    protected Cliente(Parcel in) {
        clienteId = in.readInt();
        nome = in.readString();
        codigoCliente = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(clienteId);
        dest.writeString(nome);
        dest.writeString(codigoCliente);
    }

    public static final Creator<Cliente> CREATOR = new Creator<Cliente>() {
        @Override
        public Cliente createFromParcel(Parcel in) {
            return new Cliente(in);
        }

        @Override
        public Cliente[] newArray(int size) {
            return new Cliente[size];
        }
    };

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
