package com.rtstudio.projetomeuapp.repositorio;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.rtstudio.projetomeuapp.DAO.OrdemServicoDAO;
import com.rtstudio.projetomeuapp.classes.OrdemServico;
import com.rtstudio.projetomeuapp.server.WebServicePost;
import com.rtstudio.projetomeuapp.server.WebServicePut;

import java.util.List;

/**
 * Created by Raphael Rodrigues on 16/05/2019.
 */
public class Repositorio {

    private Context mContext;
    private WebServicePost mWebServicePost = new WebServicePost();
    private WebServicePut mWebServicePut = new WebServicePut();


    public Repositorio(Context context) {
        this.mContext = context;
    }

    public boolean sicronizar(List<OrdemServico> ordens) {
        boolean sicronizou = false;
        if (checkConnection()) {
            for (OrdemServico ordem : ordens) {

                if (ordem.getSyncStatus() != OrdemServico.SYNC_STATUS_TRUE) {

                    if (ordem.getSyncStatus() == OrdemServico.SYNC_STATUS_EDITED) {
                        mWebServicePut.execute(ordem);
                    } else {
                        mWebServicePost.execute(ordem);
                    }
                    ordem.setSyncStatus(OrdemServico.SYNC_STATUS_TRUE);
                    atualizaStatusNoBanco(ordem);
                    sicronizou = true;
                }
            }
        } else {
            Toast.makeText(mContext, "Sem Internet", Toast.LENGTH_SHORT).show();
        }
        return sicronizou;
    }

    public void sicronizarEdicao(OrdemServico ordemServico) {
        if (checkConnection()) {
            mWebServicePut.execute(ordemServico);
            ordemServico.setSyncStatus(OrdemServico.SYNC_STATUS_TRUE);
        } else {
            ordemServico.setSyncStatus(OrdemServico.SYNC_STATUS_EDITED);
            Toast.makeText(mContext, "Sem Internet", Toast.LENGTH_SHORT).show();
        }
        atualizaStatusNoBanco(ordemServico);
    }

    public boolean adicionar(OrdemServico ordemServico) {
        if (salvarOrdemServicoNoBancoDeDados(ordemServico)) {
            salvarOrdemServicoNoServidor(ordemServico);
            return true;
        }
        return false;
    }

    private boolean salvarOrdemServicoNoServidor(OrdemServico ordemServico) {
        if (checkConnection()) {
            mWebServicePost.execute(ordemServico);
            atualizaStatus(ordemServico, OrdemServico.SYNC_STATUS_TRUE);
            return true;
        }
        return false;
    }

    private boolean salvarOrdemServicoNoBancoDeDados(OrdemServico ordemServico) {
        return new OrdemServicoDAO(mContext).insertOrdemServico(ordemServico);
    }

    private boolean atualizarOrdemServicoNoBancoDeDados(OrdemServico ordemServico) {
        return new OrdemServicoDAO(mContext).updateOS(ordemServico);
    }

    public void atualizaStatus(OrdemServico ordemServico, int status) {
        ordemServico.setSyncStatus(status);
        atualizaStatusNoBanco(ordemServico);
    }

    private void atualizaStatusNoBanco(OrdemServico ordemServico) {
        new OrdemServicoDAO(mContext).updateStatusOS(ordemServico);
    }

    public boolean checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
