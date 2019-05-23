package com.rtstudio.projetomeuapp.server;

import android.os.AsyncTask;

import com.rtstudio.projetomeuapp.modelo.OrdemServico;

public class WebServicePost extends AsyncTask<OrdemServico, Void, Void> {

    @Override
    protected Void doInBackground(OrdemServico... lists) {
        OrdemServico ondemServico = lists[0];

        ConnectServer.post(ondemServico);

        return null;
    }
}

