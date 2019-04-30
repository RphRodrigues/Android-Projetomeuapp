package com.rtstudio.projetomeuapp.classes;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.rtstudio.projetomeuapp.CadastrarServicoActivity;

import java.lang.ref.WeakReference;

/**
 * Created by Raphael Rodrigues on 29/04/2019.
 */
public class RequisitarEndereco extends AsyncTask<Void, Void, Endereco> {

    private WeakReference<CadastrarServicoActivity> cadastrarServicoActivityWeakReference;

    public RequisitarEndereco(CadastrarServicoActivity activity) {
        this.cadastrarServicoActivityWeakReference = new WeakReference<>(activity);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (cadastrarServicoActivityWeakReference.get() != null) {
            cadastrarServicoActivityWeakReference.get().bloquearCampos(true);
        }
    }

    @Override
    protected Endereco doInBackground(Void... voids) {

        try {
            String jsonCep = JsonRequest.requesitarJson(cadastrarServicoActivityWeakReference.get().getUriCep());

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
        if (cadastrarServicoActivityWeakReference.get() != null) {
            cadastrarServicoActivityWeakReference.get().bloquearCampos(false);

            if (endereco != null) {
                cadastrarServicoActivityWeakReference.get().setDadosEndereco(endereco);
            }
        }
    }
}
