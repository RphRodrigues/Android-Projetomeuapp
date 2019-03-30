package com.rtstudio.projetomeuapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private EditText numero;
    private EditText cidade;
    private EditText estado;
    private EditText descricaoServico;
    private Button btnCriarOS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_servico);

        nomeCliente = findViewById(R.id.edtNomeClienteId);
        rua = findViewById(R.id.edtRuaId);
        cep = findViewById(R.id.edtCepId);
        numero = findViewById(R.id.edtNumeroId);
        cidade = findViewById(R.id.edtCidadeId);
        estado = findViewById(R.id.edtEstadoId);
        descricaoServico = findViewById(R.id.edtDescricaoServicosId);
        btnCriarOS = findViewById(R.id.btnCriarOSId);

        btnCriarOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Validação para testar se o usuário inseriu os dados do cliente
                if (nomeCliente.getText().toString().isEmpty()) {
                    Toast.makeText(CadastrarServicoActivity.this,
                            getString(R.string.insira_nome_cliente),
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                //Gerar o código do cliente a partir do nome e do cpf
                String cod = nomeCliente.getText().toString().substring(0, 3);

                //Cria o cliente
                cliente = new Cliente(
                        nomeCliente.getText().toString(),
                        cod
                );

                //Validação para testar se o usuário inseriu o endereço do serviço
                if (rua.getText().toString().isEmpty()) {
                    Toast.makeText(CadastrarServicoActivity.this,
                            getString(R.string.preencha_endereco),
                            Toast.LENGTH_SHORT).show();
                    return;
                } else if (cep.getText().toString().isEmpty()) {
                    Toast.makeText(CadastrarServicoActivity.this,
                            getString(R.string.preencha_endereco),
                            Toast.LENGTH_SHORT).show();
                    return;
                } else if (numero.getText().toString().isEmpty()) {
                    Toast.makeText(CadastrarServicoActivity.this,
                            getString(R.string.preencha_endereco),
                            Toast.LENGTH_SHORT).show();
                    return;
                } else if (cidade.getText().toString().isEmpty()) {
                    Toast.makeText(CadastrarServicoActivity.this,
                            getString(R.string.preencha_endereco),
                            Toast.LENGTH_SHORT).show();
                    return;
                } else if (estado.getText().toString().isEmpty()) {
                    Toast.makeText(CadastrarServicoActivity.this,
                            getString(R.string.preencha_endereco),
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                //Cria o endereço do serviço
                endereco = new Endereco(
                        cep.getText().toString(),
                        rua.getText().toString(),
                        numero.getText().toString(),
                        cidade.getText().toString(),
                        estado.getText().toString()
                );

                //Validação para testar se o usuário inseriu a descrição do serviço
                if (descricaoServico.getText().toString().isEmpty()) {
                    Toast.makeText(CadastrarServicoActivity.this,
                            getString(R.string.preencha_descricao_servico),
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                //Cria a ordem de serviço
                ordemServico = new OrdemServico(
                        cliente,
                        endereco,
                        descricaoServico.getText().toString()
                );

                new AlertDialog.Builder(CadastrarServicoActivity.this)
                        .setTitle("Aviso")
                        .setMessage(getString(R.string.os_gerada_sucesso))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(
                                        new Intent(CadastrarServicoActivity.this,
                                                MainActivity.class)
                                );
                            }
                        })
                        .create()
                        .show();
            }
        });
    }
}
