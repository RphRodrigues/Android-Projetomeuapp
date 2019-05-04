package com.rtstudio.projetomeuapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.rtstudio.projetomeuapp.DAO.OrdemServicoDAO;
import com.rtstudio.projetomeuapp.classes.CepListener;
import com.rtstudio.projetomeuapp.classes.Cliente;
import com.rtstudio.projetomeuapp.classes.Endereco;
import com.rtstudio.projetomeuapp.classes.OrdemServico;
import com.rtstudio.projetomeuapp.classes.Utilitaria;
import com.rtstudio.projetomeuapp.preferencias.PreferenciasUsuari;

public class EditarOrdemServicoActivity extends AppCompatActivity {

    private Utilitaria util;
    Cliente cliente = null;
    Endereco endereco = null;
    OrdemServico ordemServico = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferenciasUsuari.Companion.setTema(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_ordem_servico);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ((Button) findViewById(R.id.cadastrar_btnCriarOSId)).setText("Salvar");

        util = new Utilitaria(this);

        Bundle bundleEditar = getIntent().getBundleExtra("BUNDLE");
        final OrdemServico os = bundleEditar.getParcelable("ORDEM_SERVICO");

        cliente = new Cliente();
        endereco = new Endereco();
        ordemServico = new OrdemServico();

        if (os != null) {
            cliente.setClienteId(os.getCliente().getClienteId());
            endereco.setEnderecoId(os.getEndereco().getEnderecoId());
            ordemServico.setOrdemServicoId(os.getOrdemServicoId());
            ordemServico.setFilename(os.getFilename());

            util.setDadosOrdemServico(os);

            Bitmap img = BitmapFactory.decodeFile(os.getFilename());
            ((ImageView) findViewById(R.id.cadastrar_ivBitmap)).setImageBitmap(img);
        }

        ((TextInputLayout) findViewById(R.id.cadastrar_edtCepId)).getEditText().addTextChangedListener(new CepListener(this));


        findViewById(R.id.cadastrar_btnCriarOSId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomeCliente = ((TextInputLayout) findViewById(R.id.cadastrar_edtNomeClienteId)).getEditText().getText().toString();
                String rua         = ((TextInputLayout) findViewById(R.id.cadastrar_edtRuaId)).getEditText().getText().toString();
                String complemento = ((EditText)        findViewById(R.id.cadastrar_edtComplementoId)).getText().toString();
                String bairro      = ((TextInputLayout) findViewById(R.id.cadastrar_edtBairroId)).getEditText().getText().toString();
                String cep         = ((TextInputLayout) findViewById(R.id.cadastrar_edtCepId)).getEditText().getText().toString();
                String numero      = ((TextInputLayout) findViewById(R.id.cadastrar_edtNumeroId)).getEditText().getText().toString();
                String cidade      = ((TextInputLayout) findViewById(R.id.cadastrar_edtCidadeId)).getEditText().getText().toString();
                String descrisao   = ((EditText)        findViewById(R.id.cadastrar_edtDescricaoServicosId)).getText().toString();
                String estado      = ((Spinner)         findViewById(R.id.cadastrar_spinnerEstados)).getSelectedItem().toString();
                String tipoServico = ((Spinner)         findViewById(R.id.cadastrar_spinnerTipoServico)).getSelectedItem().toString();

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
                ordemServico.setDescricaoServico(descrisao);

                if ((new OrdemServicoDAO(getBaseContext()).updateOS(ordemServico))) {
                    util.alertDialog("Aviso", "O.S. Editada com sucesso", false);
                } else {
                    util.alertDialog("Aviso", "Não foi possível editar O.S.", false);
                }
            }
        });

        findViewById(R.id.cadastrar_btnLocation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public String getUriCep() {
        return "https://viacep.com.br/ws/" + ((EditText) findViewById(R.id.cadastrar_edtCepId)).getText() + "/json/";
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
                    R.id.cadastrar_edtCidadeId,
                    R.id.cadastrar_edtDescricaoServicosId
            );
        } else if (id == R.id.menu_itemAjuda) {
            util.menuItemAjuda();
        }
        return super.onOptionsItemSelected(item);
    }
}
