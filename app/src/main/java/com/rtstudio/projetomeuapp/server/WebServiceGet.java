package com.rtstudio.projetomeuapp.server;

import android.os.AsyncTask;

import com.rtstudio.projetomeuapp.modelo.OrdemServico;

import java.util.List;

public class WebServiceGet extends AsyncTask<Void, Void, List<OrdemServico>> {

    @Override
    protected List<OrdemServico> doInBackground(Void... voids) {

        return ConnectServer.get();
    }
}
