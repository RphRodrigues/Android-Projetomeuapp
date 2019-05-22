package com.rtstudio.projetomeuapp;


import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditarProdutoFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_editar_produto, container, false);

        TextInputLayout nTextInputLayout = view.findViewById(R.id.fragment_editar_produto);

        String produto = "";

        if (getArguments() != null) {
            produto = getArguments().getString("PRODUTO");
        }

        nTextInputLayout.getEditText().setText(produto);

        return view;
    }
}
