package com.rtstudio.projetomeuapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rtstudio.projetomeuapp.adapter.OrdemServicoAdapter;
import com.rtstudio.projetomeuapp.classes.Utilitaria;
import com.rtstudio.projetomeuapp.modelo.OrdemServico;
import com.rtstudio.projetomeuapp.modelo.Usuario;
import com.rtstudio.projetomeuapp.notificacao.Notificacao;
import com.rtstudio.projetomeuapp.preferencias.PreferenciasUsuario;
import com.rtstudio.projetomeuapp.repositorio.Repositorio;

import java.util.ArrayList;
import java.util.List;

import static com.rtstudio.projetomeuapp.adapter.OrdemServicoAdapter.REQUEST_CODE_CAMERA;
import static com.rtstudio.projetomeuapp.adapter.OrdemServicoAdapter.REQUEST_CODE_CRIAR;
import static com.rtstudio.projetomeuapp.adapter.OrdemServicoAdapter.REQUEST_CODE_EDITAR;
import static com.rtstudio.projetomeuapp.adapter.OrdemServicoAdapter.REQUEST_CODE_GALERIA;

public class TelaInicialActivity extends AppCompatActivity {

    private Utilitaria mUtil;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private Repositorio mRepositorio;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private OrdemServicoAdapter adapter;
    private List<OrdemServico> ordemServicoList = null;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferenciasUsuario.Companion.setTema(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial);

        setNavigationDrawer();

        mUtil = new Utilitaria(this);

        fab = findViewById(R.id.telaInicial_fabId);

        recyclerView = findViewById(R.id.telaInicial_RecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRepositorio = new Repositorio(this);

        ordemServicoList = new ArrayList<>();
        ordemServicoList = mRepositorio.buscar();

        atualizaRecyclerView(ordemServicoList);

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

        mSwipeRefreshLayout = findViewById(R.id.telaInicial_swipeRefreshLayoutId);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                atualizaRecyclerView(mRepositorio.buscar());
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void setNavigationDrawer() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mDrawerLayout = findViewById(R.id.telaInicial_drawer);

        mNavigationView = findViewById(R.id.telaInicial_navigationView);

        Usuario usuario = null;
        if (getIntent().hasExtra("USUARIO")) {
            usuario = new Gson().fromJson(getIntent().getStringExtra("USUARIO"), Usuario.class);
        }

        if (usuario != null) {
            View headerView = mNavigationView.getHeaderView(0);
            ((TextView) headerView.findViewById(R.id.navigation_cabecalho_nome)).setText(usuario.getNome());
            ((TextView) headerView.findViewById(R.id.navigation_cabecalho_email)).setText(usuario.getEmail());
        }

        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_menu_white_24dp, getTheme()));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(mNavigationView);
            }
        });

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                mDrawerLayout.closeDrawer(mNavigationView);
                if (id == R.id.drawer_ajuda) {
                    mUtil.menuItemAjuda();
                } else if (id == R.id.drawer_sync) {
                    if (mRepositorio.sicronizar(ordemServicoList)) {
                        atualizaRecyclerView(ordemServicoList);
                    }
                } else if (id == R.id.drawer_sair) {
                    onBackPressed();
                }
                return false;
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (requestCode == REQUEST_CODE_CRIAR && resultCode == RESULT_OK) {
                OrdemServico ordemServico = new Gson().fromJson(data.getStringExtra("ORDEM_SERVICO_CRIADA"), OrdemServico.class);
                ordemServicoList.add(ordemServico);

                new Notificacao().notificacaoSimples(this, ordemServico.getEndereco().getBairro());

                atualizaRecyclerView(ordemServicoList);

            } else if (requestCode == REQUEST_CODE_EDITAR && resultCode == RESULT_OK) {
                OrdemServico ordemServico = new Gson().fromJson(data.getStringExtra("ORDEM_SERVICO_EDITADA"), OrdemServico.class);

                ordemServicoList.set(adapter.getPosicaoAtualDoClick(), ordemServico);

                atualizaRecyclerView(ordemServicoList);
            }
        }

        if (requestCode == REQUEST_CODE_GALERIA && resultCode == RESULT_OK && data != null) {

            Uri imagemSeleciona = data.getData();

            int position = adapter.getPosicaoAtualDoClick();

            String[] caminhoFile = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(imagemSeleciona, caminhoFile, null, null, null);
            cursor.moveToFirst();

            int indexColuna = cursor.getColumnIndex(caminhoFile[0]);
            String caminhoImagem = cursor.getString(indexColuna);
            cursor.close();

            ordemServicoList.get(position).setFilename(caminhoImagem);

            if (mRepositorio.atualizarImagemOrdemServico(ordemServicoList.get(position))) {
                Log.i("BANCO", "onActivityResult: galeria");

                if (adapter.isImagemAltetada()) {
                    mUtil.toast("Imagem alterada com sucesso", Toast.LENGTH_LONG);
                } else {
                    mUtil.toast("Foto salva com sucesso", Toast.LENGTH_LONG);
                }
                atualizaRecyclerView(ordemServicoList);
            }
        }

        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {

            if (adapter.getFileFoto() != null) {

                int position = adapter.getPosicaoAtualDoClick();

                String fileFotoAbsolutePath = adapter.getFileFoto().getAbsolutePath();

                ordemServicoList.get(position).setFilename(fileFotoAbsolutePath);

                if (mRepositorio.atualizarImagemOrdemServico(ordemServicoList.get(position))) {
                    Log.i("BANCO", "onActivityResult: camera");
                    if (adapter.isImagemAltetada()) {
                        mUtil.toast("Imagem alterada com sucesso", Toast.LENGTH_LONG);
                    } else {
                        mUtil.toast("Foto salva com sucesso", Toast.LENGTH_LONG);
                    }
                    atualizaRecyclerView(ordemServicoList);
                }
            }
        }
    }

    private void atualizaRecyclerView(List<OrdemServico> ordemServicoList) {
        adapter = new OrdemServicoAdapter(this, ordemServicoList);
        recyclerView.setAdapter(adapter);
    }

    private void getOrdemServicoByBairro(String bairro) {
        List<OrdemServico> ordemServicoListBairro = new ArrayList<>();

        for (OrdemServico ordemServico : ordemServicoList) {
            if (ordemServico.getEndereco().getBairro().toLowerCase().contains(bairro.toLowerCase())) {
                ordemServicoListBairro.add(ordemServico);
            }
        }
        atualizaRecyclerView(ordemServicoListBairro);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();

        EditText txtSearch = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        txtSearch.setHintTextColor(Color.LTGRAY);
        txtSearch.setTextColor(Color.WHITE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String string) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String string) {
                getOrdemServicoByBairro(string);
                return false;
            }
        });

        String tema = PreferenciasUsuario.Companion.getPreferenciaTema(this);
        if (tema.equals(PreferenciasUsuario.TEMA_NOTURNO)) {
            menu.findItem(R.id.app_bar_checkbox).setChecked(true);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.menu_itemAjuda) {
            new Utilitaria(this).menuItemAjuda();
        } else if (id == R.id.app_bar_checkbox) {
            boolean isChecked = !item.isChecked();
            item.setChecked(isChecked);

            if (item.isChecked()) {
                PreferenciasUsuario.Companion.setPreferenciaTema(this, PreferenciasUsuario.TEMA_NOTURNO);
                this.recreate();
            } else {
                PreferenciasUsuario.Companion.setPreferenciaTema(this, PreferenciasUsuario.TEMA_PADRAO);
                this.recreate();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.v("PERMISSAO", "permissão camera concedida");
                adapter.tirarFoto();
            } else {
                Log.v("PERMISSAO", "permissão camera negada");
                mUtil.toast("O acesso à câmera é necessário para adicionar/alterar uma imagem a OS.", Toast.LENGTH_SHORT * 2);
            }
        } else if (requestCode == REQUEST_CODE_GALERIA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.v("PERMISSAO", "permissão galeria concedida");
                adapter.abrirGaleria();
            } else {
                Log.v("PERMISSAO", "permissão galeria negada");
                mUtil.toast("O acesso à galeria é necessário para adicionar/alterar uma imagem a OS.", Toast.LENGTH_SHORT * 2);
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
                        MediaPlayer.create(TelaInicialActivity.this, R.raw.windows_xp_shutdown).start();
                        finish();
                    }
                })
                .create()
                .show();
    }
}