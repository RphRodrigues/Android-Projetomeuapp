package com.rtstudio.projetomeuapp.server;

import android.os.AsyncTask;

import com.rtstudio.projetomeuapp.classes.OrdemServico;

/**
 * Created by Raphael Rodrigues on 10/05/2019.
 */
public class WebServicePut extends AsyncTask<OrdemServico, Void, Void> {

    @Override
    protected Void doInBackground(OrdemServico... ordemServicos) {
        ConnectServer.put(ordemServicos[0]);
        return null;
    }
}
