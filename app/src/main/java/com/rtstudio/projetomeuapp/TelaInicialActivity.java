package com.rtstudio.projetomeuapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.rtstudio.projetomeuapp.adapter.OrdemServicoAdapter;
import com.rtstudio.projetomeuapp.classes.OrdemServico;
import com.rtstudio.projetomeuapp.classes.modeloOS;

import java.util.ArrayList;
import java.util.List;

public class TelaInicialActivity extends AppCompatActivity {

//    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private OrdemServicoAdapter adapter;
    private List<modeloOS> modeloOSList;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial);

//        fab = findViewById(R.id.telaInicial_fabId);

        modeloOSList = new ArrayList<>();

        recyclerView = findViewById(R.id.myRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        modeloOSList.add(new modeloOS(12345, "Instalação in ternet", "Raphael Rodrigues"));
        modeloOSList.add(new modeloOS(14698, "Reparo", "Thainan"));
        modeloOSList.add(new modeloOS(12345, "Instalação in ternet", "Raphael Rodrigues"));
        modeloOSList.add(new modeloOS(4569, "Manutenção"));
        modeloOSList.add(new modeloOS(12345, "Instalação in ternet", "Raphael Rodrigues"));
        modeloOSList.add(new modeloOS(14698, "Reparo", "Thainan"));
        modeloOSList.add(new modeloOS(12345, "Instalação in ternet", "Raphael Rodrigues"));
        modeloOSList.add(new modeloOS(4569, "Manutenção"));
        modeloOSList.add(new modeloOS(14698, "Reparo", "Thainan"));
        modeloOSList.add(new modeloOS(14698, "Reparo", "Thainan"));

        adapter = new OrdemServicoAdapter(this, modeloOSList);
        recyclerView.setAdapter(adapter);

//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(TelaInicialActivity.this, CadastrarServicoActivity.class));
//            }
//        });
    }
}
