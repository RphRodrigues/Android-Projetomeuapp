package com.rtstudio.projetomeuapp.notificacao;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;

import com.google.gson.Gson;
import com.rtstudio.projetomeuapp.fragment.EditarFragment;
import com.rtstudio.projetomeuapp.modelo.OrdemServico;

public class IntentServiceNotificacao extends IntentService {


    public IntentServiceNotificacao(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent.hasExtra("IDNOTIFICACAO")){
            NotificationManagerCompat.from(getBaseContext()).cancel(intent.getIntExtra("IDNOTIFICACAO", 0));

        }

        OrdemServico ordemServico = new Gson().fromJson(intent.getStringExtra("ORDEM_SERVICO"), OrdemServico.class);

        Intent i = new Intent(getBaseContext(), EditarFragment.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplication().startActivity(i);
    }
}
