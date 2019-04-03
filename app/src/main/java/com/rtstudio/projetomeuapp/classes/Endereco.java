package com.rtstudio.projetomeuapp.classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Endereco implements Parcelable {
    private String estado;
    private String cidade;
    private String rua;
    private String numero;
    private String cep;

    public Endereco(String cep, String rua, String numero, String cidade, String estado) {
        this.estado = estado;
        this.cidade = cidade;
        this.rua = rua;
        this.numero = numero;
        this.cep = cep;
    }

    protected Endereco(Parcel in) {
        estado = in.readString();
        cidade = in.readString();
        rua = in.readString();
        numero = in.readString();
        cep = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(estado);
        dest.writeString(cidade);
        dest.writeString(rua);
        dest.writeString(numero);
        dest.writeString(cep);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Endereco> CREATOR = new Creator<Endereco>() {
        @Override
        public Endereco createFromParcel(Parcel in) {
            return new Endereco(in);
        }

        @Override
        public Endereco[] newArray(int size) {
            return new Endereco[size];
        }
    };

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}
