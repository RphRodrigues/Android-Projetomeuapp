package com.rtstudio.projetomeuapp.interfaces;

import com.rtstudio.projetomeuapp.modelo.OrdemServico;

import java.io.File;
import java.util.List;

public interface InterfaceDAO {
    public void salvarArquivo(List<OrdemServico> ordens, File file);

    public List<OrdemServico> lerArquivo(File file);
}
