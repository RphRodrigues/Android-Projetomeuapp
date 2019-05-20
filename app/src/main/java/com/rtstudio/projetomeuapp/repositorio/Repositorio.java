package com.rtstudio.projetomeuapp.repositorio;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.rtstudio.projetomeuapp.DAO.OrdemServicoDAO;
import com.rtstudio.projetomeuapp.classes.OrdemServico;
import com.rtstudio.projetomeuapp.server.WebServiceDelete;
import com.rtstudio.projetomeuapp.server.WebServiceGet;
import com.rtstudio.projetomeuapp.server.WebServicePost;
import com.rtstudio.projetomeuapp.server.WebServicePut;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raphael Rodrigues on 16/05/2019.
 */
public class Repositorio {

    private Context mContext;
    private WebServicePost mWebServicePost = new WebServicePost();
    private WebServicePut mWebServicePut = new WebServicePut();
    private WebServiceDelete mWebServiceDelete = new WebServiceDelete();
    private WebServiceGet mWebServiceGet;
    private List<OrdemServico> listaOrdensBanco = new ArrayList<>();
    private List<OrdemServico> listaOrdensServico = new ArrayList<>();

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

    public boolean deletar(int ordemServicoId) {
        if (deletarOrdemServicoDoBancoDeDados(ordemServicoId)) {
            deletarOrdemServicoNoServidor(ordemServicoId);
            return true;
        }
        return false;
    }

    public List<OrdemServico> buscar() {
        listaOrdensServico = recuperarOrdemServicoDoBancoDeDados();
        recuperarOrdemServicoDoServidor();
        return listaOrdensServico;
    }

    public void retornoDoServidor(List<OrdemServico> listaOrdemServicoServidor) {

        List<OrdemServico> listaOrdemServicoServidorAux = new ArrayList<>(listaOrdemServicoServidor);
        List<OrdemServico> listaOrdemServicoBancoDedados = recuperarOrdemServicoDoBancoDeDados();

        for (OrdemServico OSServidor : listaOrdemServicoServidor) {
            for (int i = 0; i < listaOrdemServicoBancoDedados.size(); i++) {
                if (listaOrdemServicoBancoDedados.get(i).getOrdemServicoId() == OSServidor.getOrdemServicoId()) {
                    listaOrdemServicoServidorAux.remove(OSServidor);
                }
            }
        }

        if (!listaOrdemServicoServidorAux.isEmpty()) {
            for (OrdemServico os : listaOrdemServicoServidorAux) {
                os.setSyncStatus(OrdemServico.SYNC_STATUS_TRUE);
//                salvarOrdemServicoNoBancoDeDados(os);
            }
        }
    }

//    public void atualizaLista(List<OrdemServico> listaOrdemServicoServidor) {
//
//        List<OrdemServico> listaOrdemServicoServidorAux = new ArrayList<>(listaOrdemServicoServidor);
//
//        for (OrdemServico OSServidor : listaOrdemServicoServidor) {
//            for (int i = 0; i < listaOrdensServico.size(); i++) {
//                if (listaOrdensServico.get(i).getOrdemServicoId() == OSServidor.getOrdemServicoId()) {
//                    listaOrdemServicoServidorAux.remove(OSServidor);
//                }
//            }
//        }
//
//        if (listaOrdemServicoServidorAux.isEmpty()) {
//            Toast.makeText(mContext, "Lista atualizada", Toast.LENGTH_SHORT).show();
//        } else {
//            for (OrdemServico os : listaOrdemServicoServidorAux) {
//                os.setSyncStatus(OrdemServico.SYNC_STATUS_TRUE);
//            }
//            listaOrdensServico.addAll(listaOrdemServicoServidorAux);
//        }
//    }

    private boolean salvarOrdemServicoNoServidor(OrdemServico ordemServico) {
        if (checkConnection()) {
            mWebServicePost.execute(ordemServico);
            atualizaStatus(ordemServico, OrdemServico.SYNC_STATUS_TRUE);
            return true;
        }
        return false;
    }

    private void atualizarOrdemServicoNoServidor(OrdemServico ordemServico) {
        if (checkConnection()) {
            mWebServicePut.execute(ordemServico);
            ordemServico.setSyncStatus(OrdemServico.SYNC_STATUS_TRUE);
        } else {
            ordemServico.setSyncStatus(OrdemServico.SYNC_STATUS_EDITED);
        }
        atualizaStatusNoBanco(ordemServico);
    }

    private boolean deletarOrdemServicoNoServidor(int ordemServicoId) {
        if (checkConnection()) {
            mWebServiceDelete.execute(ordemServicoId);
            return true;
        }
        return false;
    }

    private boolean recuperarOrdemServicoDoServidor() {
        if (checkConnection()) {
            mWebServiceGet = new WebServiceGet(this);
//            mWebServiceGet.setTeste(new WebServiceGet.Do() {
//                @Override
//                public void atualizar(List<OrdemServico> ordemServicoList) {
//
//                }
//            });
            mWebServiceGet.execute();
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
