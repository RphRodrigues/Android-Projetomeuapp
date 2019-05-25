package com.rtstudio.projetomeuapp.notificacao;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.gson.Gson;
import com.rtstudio.projetomeuapp.R;
import com.rtstudio.projetomeuapp.modelo.OrdemServico;

public class Notificacao {

    private static final String NOTIFICACAO_SIMPLES = "NotificacaoSimples";
    private static final String NOTIFICACAO_BOTAO_ID = "NotificacaoBotão";

    public void notificacaoSimples(Context context, String bairro, String estado) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    NOTIFICACAO_SIMPLES,
                    "Notificacao Simples",
                    NotificationManager.IMPORTANCE_HIGH
            );

            notificationChannel.setDescription("Notificação simples do app");

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificacao = new NotificationCompat.Builder(context, NOTIFICACAO_SIMPLES)
                .setSmallIcon(R.drawable.notificacao)
                .setContentTitle("Nova Ordem de Serviço")
                .setContentText(bairro + " " + estado)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(1, notificacao.build());
    }

    public void notificacaoBotao(Context context, OrdemServico ordemServico) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    NOTIFICACAO_BOTAO_ID,
                    "Notificação com botão",
                    NotificationManager.IMPORTANCE_HIGH
            );

            notificationChannel.setDescription("Notificação simples do app");

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.createNotificationChannel(notificationChannel);
        }

        Intent intent = new Intent(context, IntentServiceNotificacao.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("IDNOTIFICACAO", 2);
        intent.putExtra("ORDEM_SERVICO", new Gson().toJson(ordemServico));

        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificacao = new NotificationCompat.Builder(context, NOTIFICACAO_BOTAO_ID)
                .setSmallIcon(R.drawable.notificacao)
                .setContentTitle("Projeto Rapahel")
                .setContentText("Nova Ordem de Serviço")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .addAction(android.R.drawable.ic_dialog_info, "Ver Ordem de Serviço", pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(2, notificacao.build());
    }
}
