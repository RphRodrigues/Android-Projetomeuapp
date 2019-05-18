package com.rtstudio.projetomeuapp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rtstudio.projetomeuapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CadastrarFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cadastrar, container, false);

        return view;
    }

}
