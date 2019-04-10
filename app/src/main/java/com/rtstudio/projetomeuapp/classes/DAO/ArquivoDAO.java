package com.rtstudio.projetomeuapp.classes.DAO;

import android.util.Log;

import com.rtstudio.projetomeuapp.classes.OrdemServico;
import com.rtstudio.projetomeuapp.interfaces.InterfaceDAO;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ArquivoDAO implements InterfaceDAO {

    @Override
    public void salvarArquivo(List<OrdemServico> ordens, File file) {
        ObjectOutputStream objOutput;
        try {
            objOutput = new ObjectOutputStream(new FileOutputStream(file));
            objOutput.writeObject(ordens);
            objOutput.flush();
            objOutput.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        for (OrdemServico os : ordens) {
            Log.v("Raphael", "Interface Escrevendo " + os.getEndereco().getBairro());
        }
    }

    @Override
    public List<OrdemServico> lerArquivo(File file) {

        List<OrdemServico> ordens = new ArrayList<>();
        ObjectInputStream objInput;
        try {
            objInput = new ObjectInputStream(new FileInputStream(file));
            ordens = (List) objInput.readObject();
            objInput.close();

        } catch (EOFException e) {
            e.printStackTrace();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        for (OrdemServico os : ordens) {
            Log.v("Raphael", "Interface Lendo " + os.getEndereco().getBairro());
        }
        return ordens;
    }
}
