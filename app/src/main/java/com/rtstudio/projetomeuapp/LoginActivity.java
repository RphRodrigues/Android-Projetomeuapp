package com.rtstudio.projetomeuapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rtstudio.projetomeuapp.dao.UsuarioDAO;
import com.rtstudio.projetomeuapp.modelo.Usuario;
import com.rtstudio.projetomeuapp.util.Utilitaria;
import com.rtstudio.projetomeuapp.preferencias.PreferenciasUsuario;

public class LoginActivity extends AppCompatActivity {

    private EditText mPrimeiroNome;
    private EditText mEmail;
    private Usuario mUsuario;
    private Button mButtonOk;
    private Switch mSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mPrimeiroNome = findViewById(R.id.login_nome);
        mEmail = findViewById(R.id.login_email);
        mButtonOk = findViewById(R.id.login_btnOk);
        mSwitch = findViewById(R.id.login_switch);

        if (PreferenciasUsuario.Companion.getPreferenciaLogin(this)) {
            mSwitch.setChecked(true);
            mPrimeiroNome.setText(PreferenciasUsuario.Companion.getNomeLogin(this));
            mEmail.setText(PreferenciasUsuario.Companion.getEmailLogin(this));
        } else {
            mSwitch.setChecked(false);
        }

        mButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPrimeiroNome.getText().toString().isEmpty() ||
                        mEmail.getText().toString().isEmpty()) {
                    new Utilitaria(LoginActivity.this).toast("Preencha todos os campos", Toast.LENGTH_SHORT);
                    return;
                }

                if ((mUsuario = new UsuarioDAO(getBaseContext()).getUsuarioByEmail(mEmail.getText().toString())) != null) {
                    mUsuario = new Usuario(
                            mPrimeiroNome.getText().toString(),
                            mEmail.getText().toString()
                    );
                    new UsuarioDAO(getBaseContext()).insertUsuario(mUsuario);
                }

                if (mSwitch.isChecked()) {
                    PreferenciasUsuario.Companion.setPreferenciaLogin(
                            getBaseContext(),
                            mPrimeiroNome.getText().toString().trim(),
                            mEmail.getText().toString().trim(),
                            true);
                } else {
                    PreferenciasUsuario.Companion.removePreferenciaLogin(getBaseContext(), false);
                }

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("USUARIO", new Gson().toJson(mUsuario));

                startActivity(intent);
                finish();
            }
        });
    }
}