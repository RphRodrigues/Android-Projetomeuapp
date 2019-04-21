package com.rtstudio.projetomeuapp.classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;


public class Endereco implements Parcelable, Serializable {
    private int enderecoId;
    private String estado;
    private String cidade;
    private String rua;
    private String numero;
    private String cep;
    private String bairro;
    private String complemento;

    public Endereco() {
    }

    public Endereco(String cep, String rua, String numero, String cidade, String estado, String bairro) {
        this.estado = estado;
        this.cidade = cidade;
        this.rua = rua;
        this.numero = numero;
        this.cep = cep;
        this.bairro = bairro;
    }

    public Endereco(String cep, String rua, String numero, String cidade, String estado, String bairro, String complemento) {
        this.estado = estado;
        this.cidade = cidade;
        this.rua = rua;
        this.numero = numero;
        this.cep = cep;
        this.bairro = bairro;
        this.complemento = complemento;
    }


    protected Endereco(Parcel in) {
        enderecoId = in.readInt();
        estado = in.readString();
        cidade = in.readString();
        rua = in.readString();
        numero = in.readString();
        cep = in.readString();
        bairro = in.readString();
        complemento = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(enderecoId);
        dest.writeString(estado);
        dest.writeString(cidade);
        dest.writeString(rua);
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
