package com.rtstudio.projetomeuapp.server;

import android.os.AsyncTask;

import com.rtstudio.projetomeuapp.TelaInicialActivity;
import com.rtstudio.projetomeuapp.modelo.OrdemServico;
import com.rtstudio.projetomeuapp.repositorio.Repositorio;

import java.lang.ref.WeakReference;
import java.util.List;

public class WebServiceGet extends AsyncTask<Void, Void, List<OrdemServico>> {

    @Override
    protected List<OrdemServico> doInBackground(Void... voids) {

        return ConnectServer.get();
    }
}