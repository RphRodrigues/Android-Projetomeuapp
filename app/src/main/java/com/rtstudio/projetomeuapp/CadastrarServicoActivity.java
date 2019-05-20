package com.rtstudio.projetomeuapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.rtstudio.projetomeuapp.preferencias.PreferenciasUsuario;

public class CadastrarServicoActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferenciasUsuario.Companion.setTema(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_servico);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.getOverflowIcon().setTint(getResources().getColor(R.color.white));


//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_MEMORIA);
//
//            return;
//        }
    }

    public String getUriCep() {
//        return "https://viacep.com.br/ws/" + cep.getEditText().getText() + "/json/";
        return "";
    }
}