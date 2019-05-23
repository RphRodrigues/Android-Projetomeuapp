package com.rtstudio.projetomeuapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rtstudio.projetomeuapp.DAO.UsuarioDAO;
import com.rtstudio.projetomeuapp.classes.Utilitaria;
import com.rtstudio.projetomeuapp.modelo.Usuario;

public class LoginActivity extends AppCompatActivity {

    private EditText mPrimeiroNome;
    private EditText mEmail;
    private EditText mTelefone;
    private Usuario mUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mPrimeiroNome = findViewById(R.id.login_nome);
        mEmail = findViewById(R.id.login_email);
        mTelefone = findViewById(R.id.login_telefone);

        findViewById(R.id.login_btnOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mPrimeiroNome.getText().toString().isEmpty() ||
                        mEmail.getText().toString().isEmpty() ||
                        mTelefone.getText().toString().isEmpty()) {
                    new Utilitaria(LoginActivity.this).toast("Preencha todos os campos", Toast.LENGTH_SHORT);
                    return;
                }

                if ((mUsuario = new UsuarioDAO(getBaseContext()).getUsuarioByEmail(mEmail.getText().toString())) == null) {
                    mUsuario = new Usuario(
                            mPrimeiroNome.getText().toString(),
                            mEmail.getText().toString(),
                            mTelefone.getText().toString()
                    );

                    new UsuarioDAO(getBaseContext()).insertUsuario(mUsuario);
                }

                Intent intent = new Intent(LoginActivity.this, TelaInicialActivity.class);
                intent.putExtra("USUARIO", new Gson().toJson(mUsuario));

                startActivity(intent);
                finish();
            }
        });
    }
}
