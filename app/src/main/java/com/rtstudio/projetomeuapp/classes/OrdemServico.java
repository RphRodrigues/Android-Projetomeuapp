package com.rtstudio.projetomeuapp.classes;

import java.io.Serializable;
import java.util.Random;

public class OrdemServico implements Serializable {

    public static final int SYNC_STATUS_TRUE = 1;
    public static final int SYNC_STATUS_FALSE = 0;

    private int ordemServicoId;
    private Cliente cliente;
    private Endereco endereco;
    private String tipoServico;
    private String descricaoServico;
    private String filename;
    private int syncStatus;


    //Bloco de inicialização que inicializa o id da ordem de serviço
    {
        this.ordemServicoId = gerarId();
        this.syncStatus = OrdemServico.SYNC_STATUS_FALSE;
    }

    public OrdemServico() {
    }

    public OrdemServico(int ordemServicoId, Cliente cliente, Endereco endereco, String tipoServico) {
        this.ordemServicoId = ordemServicoId;
        this.cliente = cliente;
        this.endereco = endereco;
        this.tipoServico = tipoServico;
    }

    public OrdemServico(int ordemServicoId, Cliente cliente, Endereco endereco, String filename, String tipoServico) {
        this.ordemServicoId = ordemServicoId;
        this.cliente = cliente;
        this.endereco = endereco;
        this.filename = filename;
        this.tipoServico = tipoServico;
    }

    public OrdemServico(Cliente cliente, Endereco endereco, String descricaoServico, String tipoServico) {
        this.cliente = cliente;
        this.descricaoServico = descricaoServico;
        this.endereco = endereco;
        this.tipoServico = tipoServico;
    }

    public OrdemServico(Cliente cliente, Endereco endereco, String tipoServico) {
        this.cliente = cliente;
        this.endereco = endereco;
        this.tipoServico = tipoServico;
    }

    // Soteia número entre 1.000 e 9.999 para o id da ordem de serviço
    private int gerarId() {
        return new Random().nextInt(9000) + 1000;
    }

    public int getOrdemServicoId() {
        return ordemServicoId;
    }

    public void setOrdemServicoId(int ordemServicoId) {
        this.ordemServicoId = ordemServicoId;
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

    public String getTipoServico() {
        return tipoServico;
    }

    public void setTipoServico(String tipoServico) {
        this.tipoServico = tipoServico;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(int syncStatus) {
        this.syncStatus = syncStatus;
    }
}
