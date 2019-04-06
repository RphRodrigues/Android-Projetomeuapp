package com.rtstudio.projetomeuapp;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.rtstudio.projetomeuapp.adapter.OrdemServicoAdapter;
import com.rtstudio.projetomeuapp.classes.OrdemServico;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class TelaInicialActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private OrdemServicoAdapter adapter;
    private List<OrdemServico> ordemServicoList = null;
    private ImageView imgBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial);

        imgBackground = findViewById(R.id.telaInicial_imgBg);

        fab = findViewById(R.id.telaInicial_fabId);

        recyclerView = findViewById(R.id.telaInicial_RecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (ordemServicoList == null) {
            try {
                ordemServicoList = lerAquivo();

                adapter = new OrdemServicoAdapter(this, ordemServicoList);

                recyclerView.setAdapter(adapter);

                imgBackground.setVisibility(View.INVISIBLE);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ordemServicoList = new ArrayList<>();

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

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {

            Bundle bundle = data.getBundleExtra("BUNDLE");
            if (bundle != null) {
                OrdemServico ordemServico = bundle.getParcelable("ORDEM_SERVICO");

                ordemServicoList.add(ordemServico);

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
                gravarArquivo(ordemServico);

                adapter = new OrdemServicoAdapter(this, ordemServicoList);

                recyclerView.setAdapter(adapter);

                imgBackground.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(TelaInicialActivity.this)
                .setTitle("Sair")
                .setMessage("Deseja realmente sair do app?")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .create()
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_itemAjuda) {
            String siteAjuda = "http://www.sinapseinformatica.com.br/";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(siteAjuda));
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void gravarArquivo(OrdemServico ordemServico) {

        File file = new File(getBaseContext().getFilesDir(), "meuArquivo.txt");
        try {
            ObjectOutputStream obj = new ObjectOutputStream(new FileOutputStream(file));
            obj.writeObject(ordemServico);
            obj.flush();
            obj.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<OrdemServico> lerAquivo() throws IOException {

        FileInputStream arquivo = null;
        ObjectInputStream obj = null;
        List<OrdemServico> list = null;
        try {
            arquivo = new FileInputStream(new File(getBaseContext().getFilesDir(), "meuArquivo.txt"));
            obj = new ObjectInputStream(arquivo);

            list = new ArrayList<>();


            while (true) {
                list.add((OrdemServico) obj.readObject());
            }

        } catch (EOFException e) {
            e.printStackTrace();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }
}