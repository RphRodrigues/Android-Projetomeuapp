package com.rtstudio.projetomeuapp.classes;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rtstudio.projetomeuapp.CadastrarServicoActivity;
import com.rtstudio.projetomeuapp.EditarOrdemServicoActivity;
import com.rtstudio.projetomeuapp.modelo.Endereco;

import java.lang.ref.WeakReference;

/**
 * Created by Raphael Rodrigues on 29/04/2019.
 */
public class RequisitarEndereco extends AsyncTask<Void, Void, Endereco> {

    private WeakReference<CadastrarServicoActivity> cadastrarServicoActivityWeakReference;
    private WeakReference<EditarOrdemServicoActivity> editarOrdemServicoActivityWeakReference;
    private Activity activity;
    private Utilitaria util;

    public RequisitarEndereco(CadastrarServicoActivity activity) {
        this.cadastrarServicoActivityWeakReference = new WeakReference<>(activity);
        this.activity = activity;
        util = new Utilitaria(cadastrarServicoActivityWeakReference.get());
    }

    public RequisitarEndereco(EditarOrdemServicoActivity activity) {
        this.editarOrdemServicoActivityWeakReference = new WeakReference<>(activity);
        this.activity = activity;
        util = new Utilitaria(editarOrdemServicoActivityWeakReference.get());
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Endereco doInBackground(Void... voids) {

        try {
            String jsonCep;
            if (activity instanceof CadastrarServicoActivity) {
                jsonCep = JsonRequest.requesitarJson(cadastrarServicoActivityWeakReference.get().getUriCep());
            } else {
                jsonCep = JsonRequest.requesitarJson(editarOrdemServicoActivityWeakReference.get().getUriCep());
            }

            Gson gson = new Gson();

            return gson.fromJson(jsonCep, Endereco.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Endereco endereco) {
        super.onPostExecute(endereco);
        if (!util.checkConnection()) {
            Toast.makeText(activity, "Sem internet", Toast.LENGTH_SHORT).show();
        }
        if (endereco != null) {
            if (activity instanceof CadastrarServicoActivity) {
                if (endereco.getCep() != null && cadastrarServicoActivityWeakReference.get() != null) {
                    util.setDadosEndereco(endereco);
                } else {
                    Toast.makeText(activity, "Cep inválido", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (endereco.getCep() != null && editarOrdemServicoActivityWeakReference.get() != null) {
                    util.setDadosEndereco(endereco);
                } else {
                    Toast.makeText(activity, "Cep inválido", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
