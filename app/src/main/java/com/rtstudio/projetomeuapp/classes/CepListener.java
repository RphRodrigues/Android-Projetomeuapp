package com.rtstudio.projetomeuapp.classes;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;

import com.rtstudio.projetomeuapp.CadastrarServicoActivity;
import com.rtstudio.projetomeuapp.EditarOrdemServicoActivity;

/**
 * Created by Raphael Rodrigues on 29/04/2019.
 */
public class CepListener implements TextWatcher {

    private Activity mActivity;
    private String mCep;


    public CepListener(Activity activity) {
        this.mActivity = activity;
    }

    public CepListener(Activity activity, String cep) {
        this.mActivity = activity;
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
        if (cep.length() == 8) {
            if (mActivity instanceof CadastrarServicoActivity) {
                new RequisitarEndereco((CadastrarServicoActivity) mActivity).execute();
            } else if (mActivity instanceof EditarOrdemServicoActivity) {
                new RequisitarEndereco((EditarOrdemServicoActivity) mActivity).execute();
            }
        }

        if (!cep.equals(mCep)) {
            new Utilitaria(mActivity).desbloquearBotaoSalvar(true);
        } else {
            new Utilitaria(mActivity).desbloquearBotaoSalvar(false);
        }
    }
}
