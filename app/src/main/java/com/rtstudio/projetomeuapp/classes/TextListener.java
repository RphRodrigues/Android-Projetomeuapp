package com.rtstudio.projetomeuapp.classes;

import android.app.Activity;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.rtstudio.projetomeuapp.R;
import com.rtstudio.projetomeuapp.util.Utilitaria;

public class TextListener implements TextWatcher {

    private Fragment mFragment;
    private int id;
    private String texto;

    public TextListener(Fragment fragment, int id, String texto) {
        this.mFragment = fragment;
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
            if (!texto.equals(((TextInputLayout) mFragment.getActivity().findViewById(id)).getEditText().getText().toString())) {
                new Utilitaria(mFragment).desbloquearBotaoSalvar(true);
            } else {
                new Utilitaria(mFragment).desbloquearBotaoSalvar(false);
            }
        } else {
            if (!texto.equals(((EditText) mFragment.getActivity().findViewById(id)).getText().toString())) {
                new Utilitaria(mFragment).desbloquearBotaoSalvar(true);
            } else {
                new Utilitaria(mFragment).desbloquearBotaoSalvar(false);
            }
        }
    }
}