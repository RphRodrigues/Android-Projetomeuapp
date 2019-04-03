package com.rtstudio.projetomeuapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.rtstudio.projetomeuapp.adapter.OrdemServicoAdapter;
import com.rtstudio.projetomeuapp.classes.modeloOS;

import java.util.ArrayList;
import java.util.List;

public class TelaInicialActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private OrdemServicoAdapter adapter;
    private List<modeloOS> modeloOSList;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial);

        fab = findViewById(R.id.telaInicial_fabId);

        modeloOSList = new ArrayList<>();

        recyclerView = findViewById(R.id.myRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TelaInicialActivity.this, CadastrarServicoActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {

//            Bundle bundle = data.getBundleExtra("BUNDLE");

//            if (bundle != null) {
            int numOS = data.getIntExtra("num", -1);
            String tipoServico = data.getStringExtra("tipo");
            String nomeCliente = data.getStringExtra("nome");

            modeloOSList.add(new modeloOS(numOS, tipoServico, nomeCliente));

            adapter = new OrdemServicoAdapter(this, modeloOSList);

            recyclerView.setAdapter(adapter);
//            }
        }
    }
}
