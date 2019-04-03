package com.rtstudio.projetomeuapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.rtstudio.projetomeuapp.classes.Cliente;
import com.rtstudio.projetomeuapp.classes.Endereco;
import com.rtstudio.projetomeuapp.classes.OrdemServico;

public class CadastrarServicoActivity extends AppCompatActivity {

    private Cliente cliente = null;
    private Endereco endereco = null;
    private OrdemServico ordemServico = null;

    private EditText nomeCliente;
    private EditText rua;
    private EditText cep;
    private EditText complemento;
    private EditText bairro;
    private EditText numero;
    private EditText cidade;
    private EditText descricaoServico;
    private Button btnCriarOS;
    private Spinner estado;
    private Spinner tipoServico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_servico);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nomeCliente = findViewById(R.id.cadastrar_edtNomeClienteId);
        rua = findViewById(R.id.cadastrar_edtRuaId);
        cep = findViewById(R.id.cadastrar_edtCepId);
        numero = findViewById(R.id.cadastrar_edtNumeroId);
        cidade = findViewById(R.id.cadastrar_edtCidadeId);
        estado = findViewById(R.id.cadastrar_spinnerEstados);
        complemento = findViewById(R.id.cadastrar_edtComplementoId);
        bairro = findViewById(R.id.cadastrar_edtBairroId);
        tipoServico = findViewById(R.id.cadastrar_spinnerId);
        descricaoServico = findViewById(R.id.cadastrar_edtDescricaoServicosId);
        btnCriarOS = findViewById(R.id.cadastrar_btnCriarOSId);


        btnCriarOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (!validacao()) {
//                    return;
//                }

                //Gerar o código do cliente a partir do nome e do cpf
                String codCliente = nomeCliente.getText().toString().substring(0, 3);

                //Cria o cliente
                cliente = new Cliente(
                        nomeCliente.getText().toString(),
                        codCliente
                );

                //Cria o endereço do serviço
                endereco = new Endereco(
                        cep.getText().toString(),
                        rua.getText().toString(),
                        numero.getText().toString(),
                        cidade.getText().toString(),
                        estado.getSelectedItem().toString()
                );

                //Cria a ordem de serviço
                ordemServico = new OrdemServico(
                        cliente,
                        endereco,
                        descricaoServico.getText().toString(),
                        tipoServico.getSelectedItem().toString()
                );

                Bundle bundle = new Bundle();
                bundle.putParcelable("ORDEM_SERVICO", ordemServico);

                Intent intent = new Intent();
                intent.putExtra("BUNDLE", bundle);
                setResult(RESULT_OK, intent);

                new AlertDialog.Builder(CadastrarServicoActivity.this)
                        .setTitle("Aviso")
                        .setMessage(getString(R.string.os_gerada_sucesso))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .create()
                        .show();
            }
        });
    }

    private Boolean validacao() {
        //Validação para testar se o usuário inseriu os dados do cliente
        if (nomeCliente.getText().toString().isEmpty()) {
            Toast.makeText(CadastrarServicoActivity.this,
                    getString(R.string.insira_nome_cliente),
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        //Validação para testar se o usuário inseriu o endereço do serviço
        if (rua.getText().toString().isEmpty()) {
            Toast.makeText(CadastrarServicoActivity.this,
                    getString(R.string.preencha_endereco),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (bairro.getText().toString().isEmpty()) {
            Toast.makeText(CadastrarServicoActivity.this,
                    getString(R.string.preencha_endereco),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (cep.getText().toString().isEmpty()) {
            Toast.makeText(CadastrarServicoActivity.this,
                    getString(R.string.preencha_endereco),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (numero.getText().toString().isEmpty()) {
            Toast.makeText(CadastrarServicoActivity.this,
                    getString(R.string.preencha_endereco),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (cidade.getText().toString().isEmpty()) {
            Toast.makeText(CadastrarServicoActivity.this,
                    getString(R.string.preencha_endereco),
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        //Validação para testar se o usuário inseriu a descrição do serviço
        if (descricaoServico.getText().toString().isEmpty()) {
            Toast.makeText(CadastrarServicoActivity.this,
                    getString(R.string.preencha_descricao_servico),
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.menu_itemLimpar) {
            nomeCliente.setText("");
            rua.setText("");
            complemento.setText("");
            bairro.setText("");
            cep.setText("");
            numero.setText("");
            cidade.setText("");
            descricaoServico.setText("");
        } else if (id == R.id.menu_itemAjuda) {
            String siteAjuda = "http://www.sinapseinformatica.com.br/";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(siteAjuda));
            startActivity(intent);
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