package com.rtstudio.projetomeuapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rtstudio.projetomeuapp.DAO.OrdemServicoDAO;
import com.rtstudio.projetomeuapp.adapter.OrdemServicoAdapter;
import com.rtstudio.projetomeuapp.classes.OrdemServico;
import com.rtstudio.projetomeuapp.classes.Utilitaria;
import com.rtstudio.projetomeuapp.notificacao.Notificacao;
import com.rtstudio.projetomeuapp.preferencias.PreferenciasUsuario;
import com.rtstudio.projetomeuapp.server.WebServiceGet;
import com.rtstudio.projetomeuapp.server.WebServicePost;
import com.rtstudio.projetomeuapp.service.Service;

import java.util.ArrayList;
import java.util.List;

public class TelaInicialActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_CRIAR = 1;
    public static final int REQUEST_CODE_EDITAR = 2;
    public static final int REQUEST_CODE_CAMERA = 3;
    public static final int REQUEST_CODE_GALERIA = 4;

    public static final String TEMA_PADRAO = "temaPadrao";
    public static final String TEMA_NOTURNO = "temaNoturno";

    private Utilitaria util;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private OrdemServicoAdapter adapter;
    private List<OrdemServico> ordemServicoList = null;
    private ImageView imgBackground;
    private RelativeLayout relativeLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferenciasUsuario.setTema(this);
//        PreferenciasUsuari.Companion.setTema(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial);

        setNavigationDrawer();

        util = new Utilitaria(this);

        imgBackground = findViewById(R.id.telaInicial_imgBg);

        fab = findViewById(R.id.telaInicial_fabId);

        recyclerView = findViewById(R.id.telaInicial_RecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        relativeLayout = findViewById(R.id.telaInicial_relative_layout);

        //Recupera as O.S salvas em arquivo e carrega no recyclerView
        if (ordemServicoList == null) {
//            try {
            ordemServicoList = new ArrayList<>();
//
//                ordemServicoList = new OrdemServicoDAO(this).getAll();
//
//                if (!ordemServicoList.isEmpty()) {
//                    atualizaRecyclerView(ordemServicoList);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }

        getOrdemServicoFromServer();

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
                getOrdemServicoFromServer();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void setNavigationDrawer() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final NavigationView navigationView = findViewById(R.id.telaInicial_navigationView);

        mDrawerLayout = findViewById(R.id.telaInicial_drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, 0, 0);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.isDrawerIndicatorEnabled();
        mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(navigationView);
            }
        });
        mDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                mDrawerLayout.closeDrawer(navigationView);
                if (id == R.id.drawer_sair) {
                    onBackPressed();
                }
                return false;
            }
        });

    }

    public void getOrdemServicoFromServer() {
        if (util.checkConnection()) {
            WebServiceGet webServiceGet = new WebServiceGet(this);
            webServiceGet.execute();
//            Toast.makeText(TelaInicialActivity.this, "Atualizado " + ordemServicoList.size(), Toast.LENGTH_SHORT).show();
            atualizaRecyclerView(ordemServicoList);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (requestCode == REQUEST_CODE_CRIAR && resultCode == RESULT_OK) {
                OrdemServico ordemServico = new Gson().fromJson(data.getStringExtra("ORDEM_SERVICO"), OrdemServico.class);
                ordemServicoList.add(ordemServico);

//                new OrdemServicoDAO(this).insertOrdemServico(ordemServico);
                new Notificacao().notificacaoSimples(this, ordemServico.getEndereco().getBairro());
                atualizaRecyclerView(ordemServicoList);

            } else if (requestCode == REQUEST_CODE_EDITAR && resultCode == RESULT_OK) {
                OrdemServico ordemServico = new Gson().fromJson(data.getStringExtra("ORDEM_SERVICO"), OrdemServico.class);

                ordemServicoList.set(adapter.getPosicaoAtualDoClick(), ordemServico);

                atualizaRecyclerView(ordemServicoList);
            }

        }

        if (requestCode == REQUEST_CODE_GALERIA && resultCode == RESULT_OK) {

            if (data != null) {
                Uri imagemSeleciona = data.getData();
                int pos = adapter.getPosicaoAtualDoClick();
                String[] caminhoFile = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(imagemSeleciona, caminhoFile, null, null, null);
                cursor.moveToFirst();

                int indexColuna = cursor.getColumnIndex(caminhoFile[0]);
                String caminhoImagem = cursor.getString(indexColuna);
                cursor.close();

                Bitmap imagemBitmap = BitmapFactory.decodeFile(caminhoImagem);

                if (new OrdemServicoDAO(this).addFotoParaUmaOS(pos, caminhoImagem)) {
                    Log.i("BANCO", "onActivityResult: addFotoParaUmaOS");
                    ordemServicoList = new OrdemServicoDAO(this).getAll();
                    atualizaRecyclerView(ordemServicoList);
                }
            }
        }

        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {

            if (adapter.getFileFoto() != null) {

                int position = adapter.getPosicaoAtualDoClick();

                String fileFotoAbsolutePath = adapter.getFileFoto().getAbsolutePath();

                ordemServicoList.get(position).setFilename(fileFotoAbsolutePath);

                new OrdemServicoDAO(this).addFotoParaUmaOS(ordemServicoList.get(position).getOrdemServicoId(), fileFotoAbsolutePath);

                atualizaRecyclerView(ordemServicoList);
            }
        }
    }

    public void addList(List<OrdemServico> ordensJson) {
        List<OrdemServico> ordensAUX = new ArrayList<>(ordensJson);
        for (OrdemServico ordemServico : ordensJson) {
            for (int i = 0; i < ordemServicoList.size(); i++) {
                if (ordemServicoList.get(i).getOrdemServicoId() == ordemServico.getOrdemServicoId()) {
                    ordensAUX.remove(ordemServico);
                }
            }
        }

        if (ordensAUX.size() == 0) {
            Toast.makeText(this, "Lista atualizada", Toast.LENGTH_SHORT).show();
        } else {
            ordemServicoList.addAll(ordensAUX);
        }
        atualizaRecyclerView(ordemServicoList);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.v("PERMISSAO", "permissão camera concedida");
                adapter.tirarFoto();
            } else {
                Log.v("PERMISSAO", "permissão camera negada");
                Toast.makeText(this, "O acesso à câmera é necessário para adicionar uma imagem a OS.", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == REQUEST_CODE_GALERIA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.v("PERMISSAO", "permissão galeria concedida");
                adapter.abrirGaleria();
            } else {
                Log.v("PERMISSAO", "permissão galeria negada");
                Toast.makeText(this, "O acesso à galeria é necessário para adicionar uma imagem a OS.", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void checkImageBackground() {
        if (adapter.getItemCount() > 0) {
            imgBackground.setVisibility(View.INVISIBLE);
        } else {
            imgBackground.setVisibility(View.VISIBLE);
        }
    }

    private void atualizaRecyclerView(List<OrdemServico> ordemServicoList) {

        adapter = new OrdemServicoAdapter(TelaInicialActivity.this, ordemServicoList);

        recyclerView.setAdapter(adapter);

        checkImageBackground();
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
                getOrdemServicoByBairro(string);
                return false;
            }
        });

//        String tema = PreferenciasUsuari.Companion.getPreferenciaTema(TelaInicialActivity.this);
        String tema = PreferenciasUsuario.getPreferenciaTema(TelaInicialActivity.this);
        if (tema.equals(TEMA_NOTURNO)) {
            menu.findItem(R.id.app_bar_checkbox).setChecked(true);
        }
        return super.onCreateOptionsMenu(menu);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_itemAjuda) {
            stopService(new Intent(this, Service.class));
            new Utilitaria(this).menuItemAjuda();
        } else if (id == R.id.app_bar_checkbox) {
            boolean isChecked = !item.isChecked();
            item.setChecked(isChecked);

            if (item.isChecked()) {
//                PreferenciasUsuari.Companion.setPreferenciaTema(TelaInicialActivity.this, TEMA_NOTURNO);
                PreferenciasUsuario.setPreferenciaTema(TelaInicialActivity.this, TEMA_NOTURNO);
                recreate();
            } else {
//                PreferenciasUsuari.Companion.setPreferenciaTema(TelaInicialActivity.this, TEMA_PADRAO);
                PreferenciasUsuario.setPreferenciaTema(TelaInicialActivity.this, TEMA_PADRAO);
                recreate();
            }
        } else if (id == R.id.menu_sicronizar) {
            startService(new Intent(this, Service.class));
            if (util.checkConnection()) {
                WebServicePost webServicePost = new WebServicePost();
                webServicePost.execute(ordemServicoList);
            } else {
                Toast.makeText(this, "Sem acesso internet, não é possível buscar OS", Toast.LENGTH_SHORT).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }
}