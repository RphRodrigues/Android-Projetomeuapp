package com.rtstudio.projetomeuapp.server;

import android.os.AsyncTask;

import com.rtstudio.projetomeuapp.classes.OrdemServico;
import com.rtstudio.projetomeuapp.repositorio.Repositorio;

import java.lang.ref.WeakReference;
import java.util.List;

public class WebServiceGet extends AsyncTask<Void, Void, List<OrdemServico>> {

    private Do teste = null;

    public void setTeste(Do value) {
        this.teste = value;
    }

    private WeakReference<Repositorio> repositorioWeakReference;

    public WebServiceGet(Repositorio repositorioWeakReference) {
        this.repositorioWeakReference = new WeakReference<>(repositorioWeakReference);
    }

    @Override
    protected List<OrdemServico> doInBackground(Void... voids) {

        return ConnectServer.get();
    }

    @Override
    protected void onPostExecute(List<OrdemServico> ordemServicos) {
                super.onPostExecute(ordemServicos);
                if (ordemServicos != null) {
                    for (int i = 0; i < ordemServicos.size(); i++) {
                        ordemServicos.get(i).setSyncStatus(OrdemServico.SYNC_STATUS_TRUE);
                    }
                    if (teste != null) {
                        teste.atualizar(ordemServicos);
                    }
                    repositorioWeakReference.get().retornoDoServidor(ordemServicos);
        }
    }

    public interface Do {
        void atualizar(List<OrdemServico> ordemServicoList);
}
}

