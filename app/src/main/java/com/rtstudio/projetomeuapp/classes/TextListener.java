package com.rtstudio.projetomeuapp.classes;

import android.app.Activity;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.rtstudio.projetomeuapp.R;

public class TextListener implements TextWatcher {

    private Activity mActivity;
    private int id;
    private String texto;

    public TextListener(Activity mActivity, int id, String texto) {
        this.mActivity = mActivity;
        this.id = id;
        this.texto = texto;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        if (id != R.id.cadastrar_edtComplementoId) {
            if (!texto.equals(((TextInputLayout) mActivity.findViewById(id)).getEditText().getText().toString())) {
                new Utilitaria(mActivity).desbloquearBotaoSalvar(true);
            } else {
                new Utilitaria(mActivity).desbloquearBotaoSalvar(false);
            }
        } else {
            if (!texto.equals(((EditText) mActivity.findViewById(id)).getText().toString())) {
                new Utilitaria(mActivity).desbloquearBotaoSalvar(true);
            } else {
                new Utilitaria(mActivity).desbloquearBotaoSalvar(false);
            }
        }
    }
}
