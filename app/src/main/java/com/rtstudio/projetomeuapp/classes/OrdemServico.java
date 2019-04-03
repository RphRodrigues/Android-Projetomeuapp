package com.rtstudio.projetomeuapp.classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Random;

public class OrdemServico implements Parcelable {
    private int ordemServicoId;
    private Cliente cliente;
    private Endereco endereco;
    private String tipo;
    private String descricaoServico;

    public OrdemServico(Cliente cliente, Endereco endereco, String descricaoServico, String tipo) {
        this.ordemServicoId = gerarId();
        this.cliente = cliente;
        this.descricaoServico = descricaoServico;
        this.endereco = endereco;
        this.tipo = tipo;
    }

    protected OrdemServico(Parcel in) {
        ordemServicoId = in.readInt();
        cliente = in.readParcelable(Cliente.class.getClassLoader());
        endereco = in.readParcelable(Endereco.class.getClassLoader());
        tipo = in.readString();
        descricaoServico = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ordemServicoId);
        dest.writeParcelable(cliente, flags);
        dest.writeParcelable(endereco, flags);
        dest.writeString(tipo);
        dest.writeString(descricaoServico);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OrdemServico> CREATOR = new Creator<OrdemServico>() {
        @Override
        public OrdemServico createFromParcel(Parcel in) {
            return new OrdemServico(in);
        }

        @Override
        public OrdemServico[] newArray(int size) {
            return new OrdemServico[size];
        }
    };

    // Soteia número entre 1.000 e 9.999 para o id da ordem de serviço
    private int gerarId() {
        return new Random().nextInt(10000) + 1000;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
