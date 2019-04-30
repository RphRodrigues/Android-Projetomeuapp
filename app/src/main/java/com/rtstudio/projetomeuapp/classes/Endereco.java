package com.rtstudio.projetomeuapp.classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;


public class Endereco implements Parcelable, Serializable {
    private int enderecoId;
    private String cep;
    private String logradouro;
    private String numero;
    private String bairro;
    private String localidade;
    private String uf;
    private String complemento;

    public Endereco() {
    }

    public Endereco(String cep, String logradouro, String numero, String localidade, String uf, String bairro) {
        this.uf = uf;
        this.localidade = localidade;
        this.logradouro = logradouro;
        this.numero = numero;
        this.cep = cep;
        this.bairro = bairro;
    }

    public Endereco(String cep, String logradouro, String numero, String localidade, String uf, String bairro, String complemento) {
        this.uf = uf;
        this.localidade = localidade;
        this.logradouro = logradouro;
        this.numero = numero;
        this.cep = cep;
        this.bairro = bairro;
        this.complemento = complemento;
    }


    protected Endereco(Parcel in) {
        enderecoId = in.readInt();
        uf = in.readString();
        localidade = in.readString();
        logradouro = in.readString();
        numero = in.readString();
        cep = in.readString();
        bairro = in.readString();
        complemento = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(enderecoId);
        dest.writeString(uf);
        dest.writeString(localidade);
        dest.writeString(logradouro);
        dest.writeString(numero);
        dest.writeString(cep);
        dest.writeString(bairro);
        dest.writeString(complemento);
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

    public int getEnderecoId() {
        return enderecoId;
    }

    public void setEnderecoId(int enderecoId) {
        this.enderecoId = enderecoId;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getLocalidade() {
        return localidade;
    }

    public void setLocalidade(String localidade) {
        this.localidade = localidade;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }
}
