package com.rtstudio.projetomeuapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView cadastrarServico;
    private ImageView servicoAberto;
    private ImageView servicoFechado;
    private ImageView assumirOS;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cadastrarServico = findViewById(R.id.cadastrarServicoId);
        servicoAberto = findViewById(R.id.servicosAbertosId);
        servicoFechado = findViewById(R.id.servicosFechadosId);
        assumirOS = findViewById(R.id.assumirOsId);
        fab = findViewById(R.id.floatingButtonId);

        cadastrarServico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CadastrarServicoActivity.class));
            }
        });

        servicoAberto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ServicoAbertoActivity.class));
            }
        });

        servicoFechado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ServicoFechadoActivity.class));
            }
        });

        assumirOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AssumirOSActivity.class));
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CadastrarServicoActivity.class));
            }
        });
    }
}
