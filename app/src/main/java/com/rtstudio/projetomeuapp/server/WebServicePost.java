package com.rtstudio.projetomeuapp.server;

import android.os.AsyncTask;

import com.rtstudio.projetomeuapp.classes.OrdemServico;

import java.util.List;

public class WebServicePost extends AsyncTask<List<OrdemServico>, Void, Void> {

    @Override
    protected Void doInBackground(List<OrdemServico>... lists) {
        List<OrdemServico> ondens = lists[0];

        for (OrdemServico os : ondens) {
            if (os.getSyncStatus() == OrdemServico.SYNC_STATUS_FALSE) {
                ConnectServer.post(os);
            }
        }

        return null;
    }
}

