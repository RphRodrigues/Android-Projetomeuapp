package com.rtstudio.projetomeuapp.classes;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;

import com.rtstudio.projetomeuapp.CadastrarServicoActivity;
import com.rtstudio.projetomeuapp.EditarOrdemServicoActivity;

/**
 * Created by Raphael Rodrigues on 29/04/2019.
 */
public class CepListener implements TextWatcher {

    private Context context;

    public CepListener(Context context) {
        this.context = context;
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
            if (context instanceof CadastrarServicoActivity ) {
                new RequisitarEndereco((CadastrarServicoActivity) context).execute();
            } else if (context instanceof EditarOrdemServicoActivity) {
                new RequisitarEndereco((EditarOrdemServicoActivity) context).execute();
            }
        }
    }
}
