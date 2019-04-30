package com.rtstudio.projetomeuapp.classes;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.rtstudio.projetomeuapp.CadastrarServicoActivity;

import java.lang.ref.WeakReference;

/**
 * Created by Raphael Rodrigues on 29/04/2019.
 */
public class RequisitarEndereco extends AsyncTask<Void, Void, EnderecoPOJO> {

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
    protected EnderecoPOJO doInBackground(Void... voids) {

        try {
            String jsonCep = JsonRequest.requesitarJson(cadastrarServicoActivityWeakReference.get().getUriCep());

            Gson gson = new Gson();

            return gson.fromJson(jsonCep, EnderecoPOJO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(EnderecoPOJO enderecoPOJO) {
        super.onPostExecute(enderecoPOJO);
        if (cadastrarServicoActivityWeakReference.get() != null) {
            cadastrarServicoActivityWeakReference.get().bloquearCampos(false);

            if (enderecoPOJO != null) {
                cadastrarServicoActivityWeakReference.get().setDadosEndereco(enderecoPOJO);
            }
        }
    }
}
