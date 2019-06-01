package com.rtstudio.projetomeuapp.classes;

import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;

import com.rtstudio.projetomeuapp.util.Utilitaria;

/**
 * Created by Raphael Rodrigues on 29/04/2019.
 */
public class CepListener implements TextWatcher {

    private Fragment mFragment;
    private String mCep;


    public CepListener(Fragment fragment) {
        this.mFragment = fragment;
    }

    public CepListener(Fragment activity, String cep) {
        this.mFragment = activity;
        this.mCep = cep;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String cep = s.toString();
//        if (cep.length() == 8) {
//            if (context instanceof CadastrarServicoActivity ) {
//                new RequisitarEndereco((CadastrarServicoActivity) context).execute();
//            } else if (context instanceof EditarOrdemServicoActivity) {
//                new RequisitarEndereco((EditarOrdemServicoActivity) context).execute();
//            }
//        }

        if (!cep.equals(mCep)) {
            new Utilitaria(mFragment).desbloquearBotaoSalvar(true);
        } else {
            new Utilitaria(mFragment).desbloquearBotaoSalvar(false);
        }
    }
}