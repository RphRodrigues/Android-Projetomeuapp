package com.rtstudio.projetomeuapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView cadastrarServico;
    private ImageView servicoAberto;
    private ImageView servicoFechado;
    private ImageView assumirOS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cadastrarServico = findViewById(R.id.cadastrarServicoId);
        servicoAberto = findViewById(R.id.servicosAbertosId);
        servicoFechado = findViewById(R.id.servicosFechadosId);
        assumirOS = findViewById(R.id.assumirOsId);

        cadastrarServico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CadastrarServicoActivity.class));
            }
        });

        servicoAberto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ServicoAberto.class));
            }
        });

        servicoFechado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ServicoFechado.class));
            }
        });

        assumirOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AssumirOS.class));
            }
        });
    }
}
