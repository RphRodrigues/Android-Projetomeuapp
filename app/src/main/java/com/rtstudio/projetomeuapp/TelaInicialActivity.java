package com.rtstudio.projetomeuapp;

import android.app.AlertDialog;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rtstudio.projetomeuapp.dao.OrdemServicoDAO;
import com.rtstudio.projetomeuapp.adapter.OrdemServicoAdapter;
import com.rtstudio.projetomeuapp.modelo.OrdemServico;
import com.rtstudio.projetomeuapp.util.Utilitaria;
import com.rtstudio.projetomeuapp.fragment.CadastrarFragment;
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

    public static final String TEMA_PADRAO = "temaPadrao";
    public static final String TEMA_NOTURNO = "temaNoturno";

    private ImageView imgBackground;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private OrdemServicoAdapter adapter;
    private List<OrdemServico> ordemServicoList = null;
    private Repositorio mRepositorio;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Utilitaria util;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferenciasUsuario.Companion.setTema(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial);
        setNavigationDrawer();

        mRepositorio = new Repositorio(this);

        imgBackground = findViewById(R.id.telaInicial_imgBg);

        fab = findViewById(R.id.telaInicial_fabId);

        recyclerView = findViewById(R.id.telaInicial_RecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (ordemServicoList == null) {
            try {
                ordemServicoList = new ArrayList<>();

                ordemServicoList = mRepositorio.buscar();

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

//                    getSupportFragmentManager()
//                            .beginTransaction()
//                            .disallowAddToBackStack()
//                            .replace(R.id.activity_tela_inicial_fragment_area, new TelaInicialFragment())
//                            .commit();
                if (getFragmentManager() != null) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack("INICIO")
                            .replace(R.id.activity_tela_inicial_fragment_area, new CadastrarFragment(), "CADASTRAR")
                            .commit();

                }

//                Intent intent = new Intent(TelaInicialActivity.this, CadastrarServicoActivity.class);
//                startActivityForResult(intent, 1);
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

    public void checkImageBackground() {
        if (adapter != null && adapter.getItemCount() > 0) {
            imgBackground.setVisibility(View.INVISIBLE);
        } else {
            imgBackground.setVisibility(View.VISIBLE);
        }
    }

    private void atualizaRecyclerView(List<OrdemServico> ordemServicoList) {

        adapter = new OrdemServicoAdapter(this, ordemServicoList);

        recyclerView.setAdapter(adapter);

        checkImageBackground();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);

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

        String tema = PreferenciasUsuario.Companion.getPreferenciaTema(this);
        if (tema.equals(TEMA_NOTURNO)) {
            menu.findItem(R.id.app_bar_checkbox).setChecked(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.menu_itemAjuda) {
//            stopService(new Intent(this, Service.class));
            new Utilitaria(this).menuItemAjuda();
        } else if (id == R.id.app_bar_checkbox) {
            boolean isChecked = !item.isChecked();
            item.setChecked(isChecked);

            if (item.isChecked()) {
                PreferenciasUsuario.Companion.setPreferenciaTema(this, TEMA_NOTURNO);
                recreate();
            } else {
                PreferenciasUsuario.Companion.setPreferenciaTema(this, TEMA_PADRAO);
                recreate();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void setNavigationDrawer() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.getOverflowIcon().setTint(getResources().getColor(R.color.white));

        mNavigationView = findViewById(R.id.telaInicial_navigationView);

        mDrawerLayout = findViewById(R.id.telaInicial_drawer);

        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_menu_white_24dp));
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
                    util.menuItemAjuda();
                } else if (id == R.id.drawer_sync) {
//                    getSupportFragmentManager().findFragmentByTag("");
//                    if (mRepositorio.sicronizar(ordemServicoList)) {
//                        atualizaRecyclerView(ordemServicoList);
//                    }
                } else if (id == R.id.drawer_sair) {
                    alerta();
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if ((getSupportFragmentManager().findFragmentByTag("CADASTRAR")) != null &&
                (getSupportFragmentManager().findFragmentByTag("CADASTRAR").isVisible())) {

            getSupportFragmentManager().popBackStack();

        } else if ((getSupportFragmentManager().findFragmentByTag("EDITAR")) != null &&
                (getSupportFragmentManager().findFragmentByTag("EDITAR").isVisible())) {

            getSupportFragmentManager().popBackStack();

        } else if (mDrawerLayout.isDrawerOpen(mNavigationView) ){
            mDrawerLayout.closeDrawer(mNavigationView);
        } else if (!mDrawerLayout.isDrawerOpen(mNavigationView)) {
            alerta();
        }
    }

    public void alerta() {
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
}