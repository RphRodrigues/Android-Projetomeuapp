package com.rtstudio.projetomeuapp.classes;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rtstudio.projetomeuapp.fragment.CadastrarFragment;
import com.rtstudio.projetomeuapp.fragment.EditarFragment;
import com.rtstudio.projetomeuapp.modelo.Endereco;
import com.rtstudio.projetomeuapp.util.Utilitaria;

import java.lang.ref.WeakReference;

/**
 * Created by Raphael Rodrigues on 29/04/2019.
 */
public class RequisitarEndereco extends AsyncTask<Void, Void, Endereco> {

    private WeakReference<CadastrarFragment> cadastrarServicoFragmentWeakReference;
    private WeakReference<EditarFragment> editarOrdemServicoFragmentWeakReference;
    private Fragment fragment;
    private Utilitaria util;

    public RequisitarEndereco(CadastrarFragment fragment) {
        this.cadastrarServicoFragmentWeakReference = new WeakReference<>(fragment);
        this.fragment = fragment;
//        util = new Utilitaria(cadastrarServicoActivityWeakReference.get());
    }

    public RequisitarEndereco(EditarFragment fragment) {
        this.editarOrdemServicoFragmentWeakReference = new WeakReference<>(fragment);
        this.fragment = fragment;
//        util = new Utilitaria(editarOrdemServicoActivityWeakReference.get());
    }

    @Override
    protected Endereco doInBackground(Void... voids) {

        try {
            String jsonCep;
            if (fragment instanceof CadastrarFragment) {
                jsonCep = JsonRequest.requesitarJson(cadastrarServicoFragmentWeakReference.get().getUriCep());
            } else {
                jsonCep = JsonRequest.requesitarJson(editarOrdemServicoFragmentWeakReference.get().getUriCep());
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
            Toast.makeText(fragment.getContext(), "Sem internet", Toast.LENGTH_SHORT).show();
        }
        if (endereco != null) {
            if (fragment instanceof CadastrarFragment) {
                if (endereco.getCep() != null && cadastrarServicoFragmentWeakReference.get() != null) {
                    util.bloquearCampos(false);
                    util.setDadosEndereco(endereco);
                } else {
                    Toast.makeText(fragment.getContext(), "Cep inválido", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (endereco.getCep() != null && editarOrdemServicoFragmentWeakReference.get() != null) {
                    util.bloquearCampos(false);
                    util.setDadosEndereco(endereco);
                } else {
                    Toast.makeText(fragment.getContext(), "Cep inválido", Toast.LENGTH_SHORT).show();
                }
            }
        }
        util.bloquearCampos(false);
    }
}
