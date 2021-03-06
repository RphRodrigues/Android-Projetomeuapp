package com.rtstudio.projetomeuapp.repositorio;

import android.content.Context;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.rtstudio.projetomeuapp.dao.OrdemServicoDAO;
import com.rtstudio.projetomeuapp.R;
import com.rtstudio.projetomeuapp.modelo.OrdemServico;
import com.rtstudio.projetomeuapp.server.WebServiceDelete;
import com.rtstudio.projetomeuapp.server.WebServiceGet;
import com.rtstudio.projetomeuapp.server.WebServicePost;
import com.rtstudio.projetomeuapp.server.WebServicePut;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Raphael Rodrigues on 16/05/2019.
 */
public class Repositorio {

    private Context mContext;
    private List<OrdemServico> mListaOrdens = new ArrayList<>();

    public Repositorio(Context context) {
        this.mContext = context;
    }

    public boolean sicronizar(List<OrdemServico> ordens) {
        boolean sicronizou = false;
        if (checkConnection()) {
            for (OrdemServico ordem : ordens) {

                if (ordem.getSyncStatus() != OrdemServico.SYNC_STATUS_TRUE) {

                    if (ordem.getSyncStatus() == OrdemServico.SYNC_STATUS_EDITED) {
                        new WebServicePut().execute(ordem);
                    } else {
                        new WebServicePost().execute(ordem);
                    }
                    ordem.setSyncStatus(OrdemServico.SYNC_STATUS_TRUE);
                    atualizaStatusNoBanco(ordem);
                    sicronizou = true;
                }
            }
        } else {
            Toast toast = Toast.makeText(mContext, "Sem Internet", Toast.LENGTH_LONG);
            TextView textView = toast.getView().findViewById(android.R.id.message);
            textView.setTextColor(mContext.getColor(R.color.white));
            toast.getView().getBackground().setColorFilter(mContext.getColor(R.color.myBlue), PorterDuff.Mode.SRC_IN);
            toast.show();
        }
        return sicronizou;
    }

    public boolean adicionar(OrdemServico ordemServico) {
        if (salvarOrdemServicoNoBancoDeDados(ordemServico)) {
            salvarOrdemServicoNoServidor(ordemServico);
            return true;
        }
        return false;
    }

    public boolean atualizar(OrdemServico ordemServico) {
        if (atualizarOrdemServicoNoBancoDeDados(ordemServico)) {
            atualizarOrdemServicoNoServidor(ordemServico);
            return true;
        }
        return false;
    }

    public boolean atualizarImagemOrdemServico(OrdemServico ordemServico) {
        if (atualizaImagemNoBanco(ordemServico.getOrdemServicoId(), ordemServico.getFilename())) {
            atualizarOrdemServicoNoServidor(ordemServico);
            return true;
        }
        return false;
    }

    public boolean deletar(int ordemServicoId) {
        if (deletarOrdemServicoDoBancoDeDados(ordemServicoId)) {
            deletarOrdemServicoNoServidor(ordemServicoId);
            return true;
        }
        return false;
    }

    public List<OrdemServico> buscar() {
        recuperarOrdemServicoDoServidor();
        return recuperarOrdemServicoDoBancoDeDados();
    }

    private boolean salvarOrdemServicoNoServidor(OrdemServico ordemServico) {
        if (checkConnection()) {
            new WebServicePost().execute(ordemServico);
            atualizaStatus(ordemServico, OrdemServico.SYNC_STATUS_TRUE);
            return true;
        }
        return false;
    }

    private void atualizarOrdemServicoNoServidor(OrdemServico ordemServico) {
        if (checkConnection()) {
            try {
                new WebServicePut().execute(ordemServico);
                ordemServico.setSyncStatus(OrdemServico.SYNC_STATUS_TRUE);
            } catch (IllegalStateException e) {
                ordemServico.setSyncStatus(OrdemServico.SYNC_STATUS_EDITED);
                e.printStackTrace();
            }
        } else {
            ordemServico.setSyncStatus(OrdemServico.SYNC_STATUS_EDITED);
        }
        atualizaStatusNoBanco(ordemServico);
    }

    private boolean deletarOrdemServicoNoServidor(int ordemServicoId) {
        if (checkConnection()) {
            new WebServiceDelete().execute(ordemServicoId);
            return true;
        }
        return false;
    }

    private boolean recuperarOrdemServicoDoServidor() {
        if (checkConnection()) {
            WebServiceGet mWebServiceGet = new WebServiceGet();

            try {
                mListaOrdens = mWebServiceGet.execute().get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (mListaOrdens != null) {
                for (OrdemServico ordem : mListaOrdens) {
                    if (ordem.getCliente() != null && ordem.getEndereco() != null) {
                        ordem.setSyncStatus(OrdemServico.SYNC_STATUS_TRUE);
                        if (!atualizarOrdemServicoNoBancoDeDados(ordem)) {
                            salvarOrdemServicoNoBancoDeDados(ordem);
                        }
                    }
                }
            }
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

    private boolean deletarOrdemServicoDoBancoDeDados(int ordemServicoId) {
        return new OrdemServicoDAO(mContext).deleteOrdemServico(ordemServicoId);
    }

    private List<OrdemServico> recuperarOrdemServicoDoBancoDeDados() {
        return new OrdemServicoDAO(mContext).getAll();
    }

    private void atualizaStatus(OrdemServico ordemServico, int status) {
        ordemServico.setSyncStatus(status);
        atualizaStatusNoBanco(ordemServico);
    }

    private void atualizaStatusNoBanco(OrdemServico ordemServico) {
        new OrdemServicoDAO(mContext).updateStatusOS(ordemServico);
    }

    private boolean atualizaImagemNoBanco(int ordemServicoId, String caminhoDaImagem) {
        return new OrdemServicoDAO(mContext).addFotoParaUmaOS(ordemServicoId, caminhoDaImagem);
    }

    public boolean checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
