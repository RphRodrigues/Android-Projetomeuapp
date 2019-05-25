package com.rtstudio.projetomeuapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.rtstudio.projetomeuapp.classes.CepListener;
import com.rtstudio.projetomeuapp.classes.TextListener;
import com.rtstudio.projetomeuapp.classes.Utilitaria;
import com.rtstudio.projetomeuapp.fragment.EditarProdutoFragment;
import com.rtstudio.projetomeuapp.modelo.Cliente;
import com.rtstudio.projetomeuapp.modelo.Endereco;
import com.rtstudio.projetomeuapp.modelo.OrdemServico;
import com.rtstudio.projetomeuapp.preferencias.PreferenciasUsuario;
import com.rtstudio.projetomeuapp.repositorio.Repositorio;

public class EditarOrdemServicoActivity extends AppCompatActivity {

    private Cliente mCliente = null;
    private Endereco mEndereco = null;
    private OrdemServico mOrdemServico = null;
    private Utilitaria mUtil;
    private Repositorio mRepositorio;
    private Button mBotaoSalvar;
    private Spinner mEstados;
    private Spinner mTipoServico;
    private TextInputLayout mNomeCliente;
    private TextInputLayout mRua;
    private TextInputLayout mCep;
    private TextInputLayout mBairro;
    private TextInputLayout mNumero;
    private TextInputLayout mCidade;
    private TextInputLayout mProduto;
    private EditText mComplemento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferenciasUsuario.Companion.setTema(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_ordem_servico);

        mUtil = new Utilitaria(this);

        mRepositorio = new Repositorio(this);

        inicializarVariaveis();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(mUtil.getWhiteArrow());
        toolbar.getOverflowIcon().setTint(getResources().getColor(R.color.white, getTheme()));

        mBotaoSalvar = findViewById(R.id.cadastrar_btnCriarOSId);
        mBotaoSalvar.setText(getString(R.string.salvar));
        mBotaoSalvar.setEnabled(false);
        mBotaoSalvar.setAlpha(.5f);

        final OrdemServico os = new Gson().fromJson(getIntent().getStringExtra("ORDEM_SERVICO"), OrdemServico.class);

        mCliente = new Cliente();
        mEndereco = new Endereco();
        mOrdemServico = new OrdemServico();

        if (os != null) {
            mCliente.setClienteId(os.getCliente().getClienteId());
            mEndereco.setEnderecoId(os.getEndereco().getEnderecoId());
            mOrdemServico.setOrdemServicoId(os.getOrdemServicoId());
            mOrdemServico.setFilename(os.getFilename());
            mOrdemServico.setSyncStatus(os.getSyncStatus());

            mUtil.setDadosOrdemServico(os);
        }

        mCep.getEditText().addTextChangedListener(new CepListener(this, os.getEndereco().getCep()));

        mNomeCliente.getEditText().addTextChangedListener(new TextListener(this, mNomeCliente.getId(), os.getCliente().getNome()));
        mRua.getEditText().addTextChangedListener(new TextListener(this, mRua.getId(), os.getEndereco().getLogradouro()));
        mComplemento.addTextChangedListener(new TextListener(this, mComplemento.getId(), os.getEndereco().getComplemento()));
        mBairro.getEditText().addTextChangedListener(new TextListener(this, mBairro.getId(), os.getEndereco().getBairro()));
        mNumero.getEditText().addTextChangedListener(new TextListener(this, mNumero.getId(), os.getEndereco().getNumero()));
        mCidade.getEditText().addTextChangedListener(new TextListener(this, mCidade.getId(), os.getEndereco().getLocalidade()));

        findViewById(R.id.cadastrar_btnCriarOSId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validarInputDoUsuario()) {
                    mUtil.executarSom();
                    return;
                }

                mCliente.setNome(mNomeCliente.getEditText().getText().toString());
                mCliente.setCodigoCliente(mNomeCliente.getEditText().getText().toString().substring(0, 3));

                mEndereco.setCep(mCep.getEditText().getText().toString());
                mEndereco.setLogradouro(mRua.getEditText().getText().toString());
                mEndereco.setNumero(mNumero.getEditText().getText().toString());
                mEndereco.setLocalidade(mCidade.getEditText().getText().toString());
                mEndereco.setUf(mEstados.getSelectedItem().toString());
                mEndereco.setBairro(mBairro.getEditText().getText().toString());
                mEndereco.setComplemento(mComplemento.getText().toString());

                mOrdemServico.setCliente(mCliente);
                mOrdemServico.setEndereco(mEndereco);
                mOrdemServico.setTipoServico(mTipoServico.getSelectedItem().toString());
                mOrdemServico.setProduto(mProduto.getEditText().getText().toString());

                if (mRepositorio.atualizar(mOrdemServico)) {
                    setResult(RESULT_OK, new Intent().putExtra("ORDEM_SERVICO_EDITADA", new Gson().toJson(mOrdemServico)));
                    mUtil.alertDialog("Aviso", "O.S. Editada com sucesso", false);
                } else {
                    mUtil.alertDialog("Aviso", "Não foi possível editar O.S.", false);
                }
            }
        });

        findViewById(R.id.cadastrar_btnLocation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUtil.permissaoGPS();
                mUtil.getLocalizacao();
            }
        });

        //fragment
        EditarProdutoFragment editarProdutoFragment = new EditarProdutoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("PRODUTO", os.getProduto());
        editarProdutoFragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .replace(R.id.cadastrar_container_fragment, editarProdutoFragment)
                .commit();

        mEstados.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!parent.getItemAtPosition(position).toString().equals(os.getEndereco().getUf())) {
                    mUtil.desbloquearBotaoSalvar(true);
                } else {
                    mUtil.desbloquearBotaoSalvar(false);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        mTipoServico.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!parent.getItemAtPosition(position).toString().equals(os.getTipoServico())) {
                    mUtil.desbloquearBotaoSalvar(true);
                } else {
                    mUtil.desbloquearBotaoSalvar(false);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void inicializarVariaveis() {
        mNomeCliente = findViewById(R.id.cadastrar_edtNomeClienteId);
        mRua = findViewById(R.id.cadastrar_edtRuaId);
        mComplemento = findViewById(R.id.cadastrar_edtComplementoId);
        mBairro = findViewById(R.id.cadastrar_edtBairroId);
        mCep = findViewById(R.id.cadastrar_edtCepId);
        mNumero = findViewById(R.id.cadastrar_edtNumeroId);
        mCidade = findViewById(R.id.cadastrar_edtCidadeId);
        mEstados = findViewById(R.id.cadastrar_spinnerEstados);
        mTipoServico = findViewById(R.id.cadastrar_spinnerTipoServico);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mProduto = findViewById(R.id.fragment_editar_produto);
    }

    private boolean validarInputDoUsuario() {
        return mUtil.validarCampos(R.id.cadastrar_edtNomeClienteId, R.id.cadastrar_edtRuaId, R.id.cadastrar_edtBairroId,
                R.id.cadastrar_edtCepId, R.id.cadastrar_edtCidadeId, R.id.cadastrar_edtNumeroId, R.id.cadastrar_edtComplementoId,
                R.id.fragment_editar_produto);
    }

    public String getUriCep() {
        return "https://viacep.com.br/ws/" + ((TextInputLayout) findViewById(R.id.cadastrar_edtCepId)).getEditText().getText() + "/json/";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.menu_itemLimpar) {
            mUtil.limparCampos(
                    R.id.cadastrar_edtNomeClienteId,
                    R.id.cadastrar_edtRuaId,
                    R.id.cadastrar_edtComplementoId,
                    R.id.cadastrar_edtBairroId,
                    R.id.cadastrar_edtCepId,
                    R.id.cadastrar_edtNumeroId,
                    R.id.cadastrar_edtCidadeId
            );
        } else if (id == R.id.menu_itemAjuda) {
            mUtil.menuItemAjuda();
        }
        return super.onOptionsItemSelected(item);
    }
}
