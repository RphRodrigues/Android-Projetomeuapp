package com.rtstudio.projetomeuapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.rtstudio.projetomeuapp.adapter.CustomListAdapter;
import com.rtstudio.projetomeuapp.classes.Card;

import java.util.ArrayList;
import java.util.List;

public class TelaInicialActivity extends AppCompatActivity {

//    private FloatingActionButton fab;

    private ListView listView;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_layout);

        listView = findViewById(R.id.listview);

        List<Card> lista = new ArrayList<>();
        lista.add(new Card(12345, "Instalação internet", "Raphael Rodrigues"));
        lista.add(new Card(14698, "Reparo", "Thainan"));
        lista.add(new Card(4569, "Manutenção"));
        lista.add(new Card(4569, "Manutenção"));

        CustomListAdapter adapter = new CustomListAdapter(this, R.layout.activity_tela_inicial, lista);

        listView.setAdapter(adapter);
//        fab = findViewById(R.id.telaInicial_fabId);
//
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(TelaInicialActivity.this, CadastrarServicoActivity.class));
//            }
//        });
    }
}
