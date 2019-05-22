package com.rtstudio.projetomeuapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.rtstudio.projetomeuapp.classes.CepListener;
import com.rtstudio.projetomeuapp.classes.Cliente;
import com.rtstudio.projetomeuapp.classes.Endereco;
import com.rtstudio.projetomeuapp.classes.OrdemServico;
import com.rtstudio.projetomeuapp.classes.Utilitaria;
import com.rtstudio.projetomeuapp.preferencias.PreferenciasUsuario;
import com.rtstudio.projetomeuapp.repositorio.Repositorio;

public class EditarOrdemServicoActivity extends AppCompatActivity {

    private Cliente cliente = null;
    private Endereco endereco = null;
    private OrdemServico ordemServico = null;
    private Utilitaria util;
    private Toolbar mToolbar;
    private Repositorio mRepositorio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferenciasUsuario.Companion.setTema(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_ordem_servico);

        mRepositorio = new Repositorio(this);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.getOverflowIcon().setTint(getResources().getColor(R.color.white, getTheme()));

        ((Button) findViewById(R.id.cadastrar_btnCriarOSId)).setText("Salvar");

        util = new Utilitaria(this);

        final OrdemServico os = new Gson().fromJson(getIntent().getStringExtra("ORDEM_SERVICO"), OrdemServico.class);

        cliente = new Cliente();
        endereco = new Endereco();
        ordemServico = new OrdemServico();

        if (os != null) {
            cliente.setClienteId(os.getCliente().getClienteId());
            endereco.setEnderecoId(os.getEndereco().getEnderecoId());
            ordemServico.setOrdemServicoId(os.getOrdemServicoId());
            ordemServico.setFilename(os.getFilename());
            ordemServico.setSyncStatus(os.getSyncStatus());

            util.setDadosOrdemServico(os);
        }

        ((TextInputLayout) findViewById(R.id.cadastrar_edtCepId)).getEditText().addTextChangedListener(new CepListener(this));


        findViewById(R.id.cadastrar_btnCriarOSId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validarInputDoUsuario()) {
                    util.exexutarSom();
                    return;
                }

                String nomeCliente = ((TextInputLayout) findViewById(R.id.cadastrar_edtNomeClienteId)).getEditText().getText().toString();
                String rua = ((TextInputLayout) findViewById(R.id.cadastrar_edtRuaId)).getEditText().getText().toString();
                String complemento = ((EditText) findViewById(R.id.cadastrar_edtComplementoId)).getText().toString();
                String bairro = ((TextInputLayout) findViewById(R.id.cadastrar_edtBairroId)).getEditText().getText().toString();
                String cep = ((TextInputLayout) findViewById(R.id.cadastrar_edtCepId)).getEditText().getText().toString();
                String numero = ((TextInputLayout) findViewById(R.id.cadastrar_edtNumeroId)).getEditText().getText().toString();
                String cidade = ((TextInputLayout) findViewById(R.id.cadastrar_edtCidadeId)).getEditText().getText().toString();
                String estado = ((Spinner) findViewById(R.id.cadastrar_spinnerEstados)).getSelectedItem().toString();
                String tipoServico = ((Spinner) findViewById(R.id.cadastrar_spinnerTipoServico)).getSelectedItem().toString();
                String produto = ((TextInputLayout) findViewById(R.id.fragment_editar_produto)).getEditText().getText().toString();

                cliente.setNome(nomeCliente);
                cliente.setCodigoCliente(nomeCliente.substring(0, 3));

                endereco.setCep(cep);
                endereco.setLogradouro(rua);
                endereco.setNumero(numero);
                endereco.setLocalidade(cidade);
                endereco.setUf(estado);
                endereco.setBairro(bairro);
                endereco.setComplemento(complemento);

                ordemServico.setCliente(cliente);
                ordemServico.setEndereco(endereco);
                ordemServico.setTipoServico(tipoServico);
                ordemServico.setProduto(produto);

                if (mRepositorio.atualizar(ordemServico)) {
                    setResult(RESULT_OK, new Intent().putExtra("ORDEM_SERVICO_EDITADA", new Gson().toJson(ordemServico)));
                    util.alertDialog("Aviso", "O.S. Editada com sucesso", false);
                } else {
                    util.alertDialog("Aviso", "Não foi possível editar O.S.", false);
                }
            }
        });

        findViewById(R.id.cadastrar_btnLocation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                util.getLocalizacao();
            }
        });

        EditarProdutoFragment editarProdutoFragment = new EditarProdutoFragment();
        Bundle bundle = new Bundle();
        if (os != null) {
            bundle.putString("PRODUTO", os.getProduto());
        }
        editarProdutoFragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .replace(R.id.cadastrar_container_fragment, editarProdutoFragment)
                .commit();
    }

    private boolean validarInputDoUsuario() {
        return util.validarCampos(R.id.cadastrar_edtNomeClienteId, R.id.cadastrar_edtRuaId, R.id.cadastrar_edtBairroId,
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
