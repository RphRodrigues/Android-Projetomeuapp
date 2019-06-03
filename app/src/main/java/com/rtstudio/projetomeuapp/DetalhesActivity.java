package com.rtstudio.projetomeuapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.gson.Gson;
import com.rtstudio.projetomeuapp.fragment.EditarProdutoFragment;
import com.rtstudio.projetomeuapp.modelo.OrdemServico;
import com.rtstudio.projetomeuapp.preferencias.PreferenciasUsuario;
import com.rtstudio.projetomeuapp.util.Utilitaria;

public class DetalhesActivity extends AppCompatActivity {

    private Utilitaria mUtil;
    private OrdemServico mOrdemServico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferenciasUsuario.Companion.setTema(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Detalhes O.S.");

        mUtil = new Utilitaria(this);

        if (getIntent().hasExtra("ORDEM_SERVICO")) {
            mOrdemServico = new Gson().fromJson(getIntent().getStringExtra("ORDEM_SERVICO"), OrdemServico.class);
        }

        if (getSupportFragmentManager() != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .disallowAddToBackStack()
                    .replace(R.id.cadastrar_container_fragment, new EditarProdutoFragment(mOrdemServico.getProduto()))
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mUtil.setDadosOrdemServico(mOrdemServico);
        mUtil.bloquearCampos(true,
                R.id.cadastrar_edtNomeClienteId,
                R.id.cadastrar_edtRuaId,
                R.id.cadastrar_edtCepId,
                R.id.cadastrar_edtComplementoId,
                R.id.cadastrar_edtBairroId,
                R.id.cadastrar_edtCidadeId,
                R.id.cadastrar_edtNumeroId,
                R.id.cadastrar_spinnerEstados,
                R.id.cadastrar_spinnerTipoServico
        );

        findViewById(R.id.cadastrar_btnLocation).setVisibility(View.INVISIBLE);
        findViewById(R.id.cadastrar_tvLocation).setVisibility(View.INVISIBLE);
        findViewById(R.id.fragment_cadastrar_btnCriarOSId).setVisibility(View.INVISIBLE);
    }
}
