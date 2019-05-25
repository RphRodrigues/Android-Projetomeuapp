package com.rtstudio.projetomeuapp.notificacao;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;

import com.google.gson.Gson;
import com.rtstudio.projetomeuapp.EditarOrdemServicoActivity;
import com.rtstudio.projetomeuapp.modelo.OrdemServico;

public class IntentServiceNotificacao extends IntentService {


    public IntentServiceNotificacao() {
        super("IntentServiceNotificacao");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent.hasExtra("IDNOTIFICACAO")) {
            NotificationManagerCompat.from(getBaseContext()).cancel(intent.getIntExtra("IDNOTIFICACAO", 0));
        }

        OrdemServico ordemServico = new Gson().fromJson(intent.getStringExtra("ORDEM_SERVICO"), OrdemServico.class);

        Intent intentEditar = new Intent(getBaseContext(), EditarOrdemServicoActivity.class);
        intentEditar.putExtra("ORDEM_SERVICO", new Gson().toJson(ordemServico));
        intentEditar.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplication().startActivity(intentEditar);
    }
}
