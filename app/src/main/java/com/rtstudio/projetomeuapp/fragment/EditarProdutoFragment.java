package com.rtstudio.projetomeuapp.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rtstudio.projetomeuapp.R;
import com.rtstudio.projetomeuapp.util.Utilitaria;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditarProdutoFragment extends Fragment {

    private String mProduto = "";
    private TextInputLayout mTextInputLayout;
    private Utilitaria mUtil;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_editar_produto, container, false);

        mTextInputLayout = view.findViewById(R.id.fragment_editar_produto_TextInputLayout);

        mUtil = new Utilitaria(this);

        if (getArguments() != null) {
            mProduto = getArguments().getString("PRODUTO");
        }

        mTextInputLayout.getEditText().setText(mProduto);

        mTextInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!mTextInputLayout.getEditText().getText().toString().equals(mProduto)) {
                    mUtil.desbloquearBotaoSalvar(true);
                } else {
                    mUtil.desbloquearBotaoSalvar(false);
                }
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("FRAG", "onStart: EditarProdutoFragement");
    }
}