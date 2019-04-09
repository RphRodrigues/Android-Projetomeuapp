package com.rtstudio.projetomeuapp.classes.DAO;

import com.rtstudio.projetomeuapp.classes.OrdemServico;
import com.rtstudio.projetomeuapp.interfaces.InterfaceDAO;

import java.io.File;
import java.util.List;

public class BancoDeDadosDAO implements InterfaceDAO {

    @Override
    public void salvarBD() {

    }

    @Override
    public void lerBD() {

    }

    @Override
    public void salvarArquivo(List<OrdemServico> ordens, File file) {

    }

    @Override
    public List<OrdemServico> lerArquivo(File file) {
        return null;
    }
}
