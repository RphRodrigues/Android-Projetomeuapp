package com.rtstudio.projetomeuapp.fragment;


import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rtstudio.projetomeuapp.R;
import com.rtstudio.projetomeuapp.classes.CepListener;
import com.rtstudio.projetomeuapp.modelo.Cliente;
import com.rtstudio.projetomeuapp.modelo.Endereco;
import com.rtstudio.projetomeuapp.modelo.OrdemServico;
import com.rtstudio.projetomeuapp.util.Utilitaria;
import com.rtstudio.projetomeuapp.repositorio.Repositorio;
import com.rtstudio.projetomeuapp.viewModel.MyViewModel;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.support.v4.content.ContextCompat.checkSelfPermission;

/**
 * A simple {@link Fragment} subclass.
 */
public class CadastrarFragment extends Fragment {

    public static final int PERMISSION_REQUEST_GPS = 100;
    public static final int PERMISSION_REQUEST_MEMORIA = 101;

    private Cliente mCliente = null;
    private Endereco mEndereco = null;
    private OrdemServico mOrdemServico = null;
    private TextInputLayout nomeCliente;
    private TextInputLayout rua;
    private TextInputLayout cep;
    private EditText complemento;
    private TextInputLayout bairro;
    private TextInputLayout numero;
    private TextInputLayout cidade;
    private Spinner estado;
    private Spinner tipoServico;
    private Repositorio mRepositorio;
    private Utilitaria util;
    private MyViewModel mMyViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cadastrar, container, false);

        getActivity().setTitle(getString(R.string.cadastrar_servico));

        mRepositorio = new Repositorio(getContext());

        inicilizarVariaveisDeClasse(view);

        cep.getEditText().addTextChangedListener(new CepListener(getContext()));

        util = new Utilitaria(this, view);


        view.findViewById(R.id.cadastrar_btnCriarOSId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validarInputDoUsuario()) {
                    return;
                }

                createCliente();

                createEndereco();

                createOrdemServico();

                if (mRepositorio.adicionar(mOrdemServico)) {
                    getActivity().setResult(RESULT_OK, new Intent().putExtra("ORDEM_SERVICO_CRIADA", new Gson().toJson(mOrdemServico)));
                    util.alertDialog("Aviso", getString(R.string.os_gerada_sucesso), false);
                } else {
                    getActivity().setResult(RESULT_CANCELED);
                    util.alertDialog("Aviso", "Não foi possível criar O.S.", false);
                }
            }
        });


        view.findViewById(R.id.cadastrar_btnLocation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        || (checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {

                    String[] permissoes = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

                    ActivityCompat.requestPermissions(getActivity(), permissoes, PERMISSION_REQUEST_GPS);
                    return;
                }
                util.getLocalizacao();
            }
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        mMyViewModel = ViewModelProviders.of(getActivity()).get(MyViewModel.class);
        if (mOrdemServico != null) {
            mMyViewModel.criarOS(mOrdemServico);
        }
    }

    private boolean validarInputDoUsuario() {
        return util.validarCampos(R.id.cadastrar_edtNomeClienteId, R.id.cadastrar_edtRuaId, R.id.cadastrar_edtBairroId,
                R.id.cadastrar_edtCepId, R.id.cadastrar_edtCidadeId, R.id.cadastrar_edtNumeroId, R.id.cadastrar_edtComplementoId);
    }

    private void inicilizarVariaveisDeClasse(View view) {
        nomeCliente = view.findViewById(R.id.cadastrar_edtNomeClienteId);
        rua = view.findViewById(R.id.cadastrar_edtRuaId);
        cep = view.findViewById(R.id.cadastrar_edtCepId);
        numero = view.findViewById(R.id.cadastrar_edtNumeroId);
        cidade = view.findViewById(R.id.cadastrar_edtCidadeId);
        estado = view.findViewById(R.id.cadastrar_spinnerEstados);
        complemento = view.findViewById(R.id.cadastrar_edtComplementoId);
        bairro = view.findViewById(R.id.cadastrar_edtBairroId);
        tipoServico = view.findViewById(R.id.cadastrar_spinnerTipoServico);

        //Inicializa o spinner de estados com RJ
        estado.setSelection(18);
    }

    private void createOrdemServico() {
        mOrdemServico = new OrdemServico(
                mCliente,
                mEndereco,
                tipoServico.getSelectedItem().toString()
        );
    }

    private void createEndereco() {
        mEndereco = new Endereco(
                cep.getEditText().getText().toString(),
                rua.getEditText().getText().toString(),
                numero.getEditText().getText().toString(),
                cidade.getEditText().getText().toString(),
                estado.getSelectedItem().toString(),
                bairro.getEditText().getText().toString(),
                complemento.getText().toString()
        );
    }

    private void createCliente() {
        mCliente = util.createCliente(nomeCliente.getEditText().getText().toString());
    }

    public String getUriCep() {
        return "https://viacep.com.br/ws/" + cep.getEditText().getText() + "/json/";
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_GPS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.v("PERMISSAO", "Permissão gps concedida");
                util.getLocalizacao();
            } else {
                Log.v("PERMISSAO", "Permissão gps negada");
                Toast.makeText(getContext(), "O acesso a localização é necessário para utilizar o GPS.", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == PERMISSION_REQUEST_MEMORIA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.v("PERMISSAO", "Permissão memória concedida");

            } else {
                Log.v("PERMISSAO", "Permissão memória negada");
                Toast.makeText(getContext(), "O acesso à memória é necessário para criar OS", Toast.LENGTH_LONG).show();
                getActivity().finish();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            getActivity().finish();
        } else if (id == R.id.menu_itemLimpar) {
            util.limparCampos(
                    R.id.cadastrar_edtNomeClienteId,
                    R.id.cadastrar_edtRuaId,
                    R.id.cadastrar_edtComplementoId,
                    R.id.cadastrar_edtBairroId,
                    R.id.cadastrar_edtCepId,
                    R.id.cadastrar_edtNumeroId,
                    R.id.cadastrar_edtCidadeId
            );
        } else if (id == R.id.menu_itemAjuda) {
            util.menuItemAjuda();
        }
        return super.onOptionsItemSelected(item);
    }
}
