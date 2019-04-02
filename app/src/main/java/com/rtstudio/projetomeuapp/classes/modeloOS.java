package com.rtstudio.projetomeuapp.classes;

/**
 * Created by Raphael Rodrigues on 31/03/2019
 */
public class modeloOS {
    private int numOS;
    private String tipoServico;
    private String nomeCliente;

    public modeloOS(int numOS, String tipoServico, String nomeCliente) {
        this.numOS = numOS;
        this.tipoServico = tipoServico;
        this.nomeCliente = nomeCliente;
    }

    public modeloOS(int numOS, String tipoServico) {
        this.numOS = numOS;
        this.tipoServico = tipoServico;
    }

    public int getNumOS() {
        return numOS;
    }

    public void setNumOS(int numOS) {
        this.numOS = numOS;
    }

    public String getTipoServico() {
        return tipoServico;
    }

    public void setTipoServico(String tipoServico) {
        this.tipoServico = tipoServico;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }
}
