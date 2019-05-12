package com.rtstudio.projetomeuapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
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
import com.rtstudio.projetomeuapp.DAO.OrdemServicoDAO;
import com.rtstudio.projetomeuapp.classes.CepListener;
import com.rtstudio.projetomeuapp.classes.Cliente;
import com.rtstudio.projetomeuapp.classes.Endereco;
import com.rtstudio.projetomeuapp.classes.OrdemServico;
import com.rtstudio.projetomeuapp.classes.Utilitaria;
import com.rtstudio.projetomeuapp.preferencias.PreferenciasUsuario;

public class CadastrarServicoActivity extends AppCompatActivity {

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
    private Utilitaria util;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferenciasUsuario.setTema(this);
//        PreferenciasUsuari.Companion.setTema(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_servico);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inicilizarVariaveisDeClasse();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_MEMORIA);

            return;
        }

        cep.getEditText().addTextChangedListener(new CepListener(this));

        util = new Utilitaria(this);

        findViewById(R.id.cadastrar_btnCriarOSId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validarInputDoUsuario()) {
                    return;
                }

                createCliente();

                createEndereco();

                createOrdemServico();

                if (salvarOrdemServicoNoBancoDeDados()) {
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
                if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        || (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {

                    String[] permissoes = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

                    ActivityCompat.requestPermissions(CadastrarServicoActivity.this, permissoes, PERMISSION_REQUEST_GPS);
                    return;
                }
                util.getLocalizacao();
            }
        });
    }

    private boolean validarInputDoUsuario() {
        return util.validarCampos(R.id.cadastrar_edtNomeClienteId, R.id.cadastrar_edtRuaId, R.id.cadastrar_edtBairroId,
                R.id.cadastrar_edtCepId, R.id.cadastrar_edtCidadeId, R.id.cadastrar_edtNumeroId, R.id.cadastrar_edtComplementoId);
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

    private boolean salvarOrdemServicoNoBancoDeDados() {
        return new OrdemServicoDAO(CadastrarServicoActivity.this).insertOrdemServico(mOrdemServico);
    }

    private boolean atualizarOrdemServicoNoBancoDeDados() {
        return new OrdemServicoDAO(CadastrarServicoActivity.this).updateOS(mOrdemServico);
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

    public void bloquearCampos(boolean isBloquear) {
        util.bloquearCampos(isBloquear,
                R.id.cadastrar_edtRuaId,
                R.id.cadastrar_edtComplementoId,
                R.id.cadastrar_edtBairroId,
                R.id.cadastrar_edtCepId,
                R.id.cadastrar_edtNumeroId,
                R.id.cadastrar_edtCidadeId
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_GPS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.v("PERMISSAO", "Permissão gps concedida");
                util.getLocalizacao();
            } else {
                Log.v("PERMISSAO", "Permissão gps negada");
                Toast.makeText(this, "O acesso a localização é necessário para utilizar o GPS.", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == PERMISSION_REQUEST_MEMORIA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.v("PERMISSAO", "Permissão memória concedida");

            } else {
                Log.v("PERMISSAO", "Permissão memória negada");
                Toast.makeText(this, "O acesso à memória é necessário para criar OS", Toast.LENGTH_LONG).show();
                finish();
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