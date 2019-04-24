package com.rtstudio.projetomeuapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.rtstudio.projetomeuapp.adapter.OrdemServicoAdapter;
import com.rtstudio.projetomeuapp.classes.DAO.ArquivoDAO;
import com.rtstudio.projetomeuapp.classes.OrdemServico;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TelaInicialActivity extends AppCompatActivity {

    public static final int PERMISSION_REQUEST_CAMERA = 1;
    public static final int PERMISSION_REQUEST_GALERIA = 2;

    File file;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private OrdemServicoAdapter adapter;
    private List<OrdemServico> ordemServicoList = null;
    private ImageView imgBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial);

        file = new File(getBaseContext().getFilesDir(), "TCC.txt");

        imgBackground = findViewById(R.id.telaInicial_imgBg);

        fab = findViewById(R.id.telaInicial_fabId);

        recyclerView = findViewById(R.id.telaInicial_RecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Recupera as O.S salvas em arquivo e carrega no recyclerView
        if (ordemServicoList == null) {
            try {
                ordemServicoList = new ArrayList<>();

                ordemServicoList = new ArquivoDAO().lerArquivo(file);

                if (!ordemServicoList.isEmpty()) {
                    atualizaRecyclerView(ordemServicoList);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    fab.hide();
                } else {
                    fab.show();
                }
            }
        });

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

        Bundle bundle;
        if (data != null) {
            bundle = data.getBundleExtra("BUNDLE");
            if (requestCode == 1 && resultCode == RESULT_OK && bundle != null) {

                OrdemServico ordemServico = bundle.getParcelable("ORDEM_SERVICO");

                ordemServicoList.add(ordemServico);

                //Grava a lista de O.S. em arquivo .txt
                new ArquivoDAO().salvarArquivo(ordemServicoList, file);

                atualizaRecyclerView(ordemServicoList);

            } else if (requestCode == 2 && bundle != null) {

                OrdemServico os = bundle.getParcelable("ORDEM_SERVICO");

                int pos = bundle.getInt("POSITION");

                ordemServicoList.set(pos, os);

                //Grava a lista de O.S. em arquivo .txt
                new ArquivoDAO().salvarArquivo(ordemServicoList, file);

                atualizaRecyclerView(ordemServicoList);

            }

        }

        if (requestCode == 3 && resultCode == RESULT_OK) {

            if (adapter.getFileFoto() != null) {

                String fileFotoAbsolutePath = adapter.getFileFoto().getAbsolutePath();
                int inicio = fileFotoAbsolutePath.lastIndexOf("Foto") + 4;
                int fim = fileFotoAbsolutePath.lastIndexOf("jpg") - 1;
                int pos = Integer.parseInt(fileFotoAbsolutePath.substring(inicio, fim));

                ordemServicoList.get(pos).setFile(fileFotoAbsolutePath);

                new ArquivoDAO().salvarArquivo(ordemServicoList, file);

                atualizaRecyclerView(ordemServicoList);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.v("PERMISSAO", "permissão camera concedida");
//                OrdemServicoAdapter.camera(this, 0);
            } else {
                Log.v("PERMISSAO", "permissão camera negada");
                Toast.makeText(this, "O acesso à câmera é necessário para adicionar uma imagem a OS.", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == PERMISSION_REQUEST_GALERIA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.v("PERMISSAO", "permissão galeria concedida");
            } else {
                Log.v("PERMISSAO", "permissão galeria negada");
            }
        }
    }

    private void atualizaRecyclerView(List<OrdemServico> ordemServicoList) {

        adapter = new OrdemServicoAdapter(TelaInicialActivity.this, ordemServicoList);

        recyclerView.setAdapter(adapter);

        imgBackground.setVisibility(View.INVISIBLE);
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

        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String string) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String string) {
                return false;
            }
        });

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
}