package com.rtstudio.projetomeuapp.fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.rtstudio.projetomeuapp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class CadastrarProdutoFragment extends Fragment {

    private String mProduto;
    private passagemDeDados mPassagem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_produto_cadastrar, container, false);

        view.findViewById(R.id.fragment_btnProduto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.findViewById(R.id.fragment_tvEscolha).setVisibility(View.INVISIBLE);

                View alertView = getLayoutInflater().inflate(R.layout.alerta_dialog_produto, null);

                new AlertDialog.Builder(view.getContext())
                        .setView(alertView)
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                RadioGroup radioGroup = alertView.findViewById(R.id.alerta_produto_radioGroup);
                                RadioButton radioButton = alertView.findViewById(radioGroup.getCheckedRadioButtonId());
                                mProduto = radioButton.getText().toString();

                                passarDados(mProduto);
                            }
                        })
                        .create()
                        .show();
            }
        });
        return view;
    }

    public void passarDados(String dados) {
        mPassagem.passarDados(dados);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mPassagem = (passagemDeDados) context;
    }

    public interface passagemDeDados {
        void passarDados(String dados);
    }
}
