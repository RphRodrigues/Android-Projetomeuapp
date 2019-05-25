package com.rtstudio.projetomeuapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rtstudio.projetomeuapp.classes.CepListener;
import com.rtstudio.projetomeuapp.classes.Utilitaria;
import com.rtstudio.projetomeuapp.fragment.CadastrarProdutoFragment;
import com.rtstudio.projetomeuapp.modelo.Cliente;
import com.rtstudio.projetomeuapp.modelo.Endereco;
import com.rtstudio.projetomeuapp.modelo.OrdemServico;
import com.rtstudio.projetomeuapp.preferencias.PreferenciasUsuario;
import com.rtstudio.projetomeuapp.repositorio.Repositorio;

public class CadastrarServicoActivity extends AppCompatActivity implements CadastrarProdutoFragment.passagemDeDados {

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
    private Utilitaria util;
    private Repositorio mRepositorio;
    private String mProduto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferenciasUsuario.Companion.setTema(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_servico);

        util = new Utilitaria(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(util.getWhiteArrow());
        toolbar.getOverflowIcon().setTint(getResources().getColor(R.color.white, getTheme()));

        mRepositorio = new Repositorio(this);

        inicilizarVariaveisDeClasse();

        cep.getEditText().addTextChangedListener(new CepListener(this));

        findViewById(R.id.cadastrar_btnCriarOSId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validarInputDoUsuario() || !validarRadioButton()) {
                    util.executarSom();
                    if (!validarRadioButton()) {
                        findViewById(R.id.fragment_tvEscolha).setVisibility(View.VISIBLE);
                    }
                    return;
                }

                createCliente();

                createEndereco();

                createOrdemServico();

                if (mRepositorio.adicionar(mOrdemServico)) {
                    setResult(RESULT_OK, new Intent().putExtra("ORDEM_SERVICO_CRIADA", new Gson().toJson(mOrdemServico)));
                    util.alertDialog("Aviso", getString(R.string.os_gerada_sucesso), false);
                } else {
                    setResult(RESULT_CANCELED);
                    util.alertDialog("Aviso", "Não foi possível criar O.S.", false);
                }
            }
        });

        findViewById(R.id.cadastrar_btnLocation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                util.permissaoGPS();
                util.getLocalizacao();
            }
        });

        getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .replace(R.id.cadastrar_container_fragment, new CadastrarProdutoFragment())
                .commit();
    }

    @Override
    public void passarDados(String dados) {
        mProduto = dados;
    }

    private boolean validarInputDoUsuario() {
        return util.validarCampos(R.id.cadastrar_edtNomeClienteId, R.id.cadastrar_edtRuaId, R.id.cadastrar_edtBairroId,
                R.id.cadastrar_edtCepId, R.id.cadastrar_edtCidadeId, R.id.cadastrar_edtNumeroId, R.id.cadastrar_edtComplementoId);
    }

    private boolean validarRadioButton() {
        return mProduto != null;
    }

    private void inicilizarVariaveisDeClasse() {
        nomeCliente = findViewById(R.id.cadastrar_edtNomeClienteId);
        rua = findViewById(R.id.cadastrar_edtRuaId);
        cep = findViewById(R.id.cadastrar_edtCepId);
        numero = findViewById(R.id.cadastrar_edtNumeroId);
        cidade = findViewById(R.id.cadastrar_edtCidadeId);
        estado = findViewById(R.id.cadastrar_spinnerEstados);
        complemento = findViewById(R.id.cadastrar_edtComplementoId);
        bairro = findViewById(R.id.cadastrar_edtBairroId);
        tipoServico = findViewById(R.id.cadastrar_spinnerTipoServico);

        //Inicializa o spinner de estados com RJ
        estado.setSelection(18);
    }

    private void createOrdemServico() {
        mOrdemServico = new OrdemServico(
                mCliente,
                mEndereco,
                tipoServico.getSelectedItem().toString(),
                mProduto
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

        if (requestCode == Utilitaria.PERMISSION_REQUEST_GPS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.v("PERMISSAO", "Permissão gps concedida");
                util.getLocalizacao();
            } else {
                Log.v("PERMISSAO", "Permissão gps negada");
                util.toast("O acesso a localização é necessário para utilizar o GPS.", Toast.LENGTH_LONG * 2);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}