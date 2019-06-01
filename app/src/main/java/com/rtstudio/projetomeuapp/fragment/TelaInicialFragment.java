package com.rtstudio.projetomeuapp.fragment;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rtstudio.projetomeuapp.R;
import com.rtstudio.projetomeuapp.adapter.OrdemServicoAdapter;
import com.rtstudio.projetomeuapp.modelo.OrdemServico;
import com.rtstudio.projetomeuapp.notificacao.Notificacao;
import com.rtstudio.projetomeuapp.preferencias.PreferenciasUsuario;
import com.rtstudio.projetomeuapp.repositorio.Repositorio;
import com.rtstudio.projetomeuapp.util.Utilitaria;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.rtstudio.projetomeuapp.adapter.OrdemServicoAdapter.REQUEST_CODE_CAMERA;
import static com.rtstudio.projetomeuapp.adapter.OrdemServicoAdapter.REQUEST_CODE_CRIAR;
import static com.rtstudio.projetomeuapp.adapter.OrdemServicoAdapter.REQUEST_CODE_EDITAR;
import static com.rtstudio.projetomeuapp.adapter.OrdemServicoAdapter.REQUEST_CODE_GALERIA;

/**
 * A simple {@link Fragment} subclass.
 */
public class TelaInicialFragment extends Fragment {

    private ImageView imgBackground;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private OrdemServicoAdapter adapter;
    private List<OrdemServico> ordemServicoList = null;
    private Repositorio mRepositorio;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Utilitaria mUtil;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tela_inicial, container, false);

        getActivity().setTitle(getString(R.string.app_name));

        mRepositorio = new Repositorio(getContext());

        mUtil = new Utilitaria(this);

        imgBackground = view.findViewById(R.id.telaInicial_imgBg);

        fab = view.findViewById(R.id.telaInicial_fabId);

        recyclerView = view.findViewById(R.id.telaInicial_RecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

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

                if (getFragmentManager() != null) {
                    getFragmentManager()
                            .beginTransaction()
                            .addToBackStack("INICIO")
                            .replace(R.id.main_activity_fragment_area, new CadastrarFragment(), "CADASTRAR")
                            .commit();
                }
            }
        });


        mSwipeRefreshLayout = view.findViewById(R.id.telaInicial_swipeRefreshLayoutId);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                atualizaRecyclerView(mRepositorio.buscar());
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ordemServicoList.clear();
        ordemServicoList = mRepositorio.buscar();
        atualizaRecyclerView(ordemServicoList);
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
                PreferenciasUsuario.Companion.setPreferenciaTema(getContext(), PreferenciasUsuario.TEMA_NOTURNO);
                getActivity().recreate();
            } else {
                PreferenciasUsuario.Companion.setPreferenciaTema(getContext(), PreferenciasUsuario.TEMA_PADRAO);
                getActivity().recreate();
            }
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data != null) {
            if (requestCode == REQUEST_CODE_CRIAR && resultCode == RESULT_OK) {
                OrdemServico ordemServico = new Gson().fromJson(data.getStringExtra("ORDEM_SERVICO_CRIADA"), OrdemServico.class);
                ordemServicoList.add(ordemServico);

                new Notificacao().notificacaoSimples(getContext(), ordemServico.getEndereco().getBairro());

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

            Cursor cursor = getActivity().getContentResolver().query(imagemSeleciona, caminhoFile, null, null, null);
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

        super.onActivityResult(requestCode, resultCode, data);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
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

        String tema = PreferenciasUsuario.Companion.getPreferenciaTema(getContext());
        if (tema.equals(PreferenciasUsuario.TEMA_NOTURNO)) {
            menu.findItem(R.id.app_bar_checkbox).setChecked(true);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.v("PERMISSAO", "permissão camera concedida");
                adapter.tirarFoto();
            } else {
                Log.v("PERMISSAO", "permissão camera negada");
                mUtil.toast("O acesso à câmera é necessário para selecionar uma imagem", Toast.LENGTH_SHORT * 2);
            }
        } else if (requestCode == REQUEST_CODE_GALERIA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.v("PERMISSAO", "permissão galeria concedida");
                adapter.abrirGaleria();
            } else {
                Log.v("PERMISSAO", "permissão galeria negada");
                mUtil.toast("O acesso à galeria é necessário para selecionar uma imagem", Toast.LENGTH_SHORT * 2);
            }
        }
    }
}
