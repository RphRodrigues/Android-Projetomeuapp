package com.rtstudio.projetomeuapp.server;

import android.os.AsyncTask;

import com.rtstudio.projetomeuapp.TelaInicialActivity;
import com.rtstudio.projetomeuapp.classes.OrdemServico;

import java.lang.ref.WeakReference;
import java.util.List;

public class WebServiceGet extends AsyncTask<Void, Void, List<OrdemServico>> {

    WeakReference<TelaInicialActivity> telaInicialActivityWeakReference;

    public WebServiceGet(TelaInicialActivity telaInicialActivityWeakReference) {
        this.telaInicialActivityWeakReference = new WeakReference<>(telaInicialActivityWeakReference);
    }

    @Override
    protected List<OrdemServico> doInBackground(Void... voids) {

        return ConnectServer.get();
    }

    @Override
    protected void onPostExecute(List<OrdemServico> ordemServicos) {
        super.onPostExecute(ordemServicos);
        if (ordemServicos != null) {
            telaInicialActivityWeakReference.get().addList(ordemServicos);
        }
    }
}
