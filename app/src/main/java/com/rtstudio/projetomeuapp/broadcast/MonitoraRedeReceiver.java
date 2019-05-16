package com.rtstudio.projetomeuapp.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class MonitoraRedeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Acão brradcast", Toast.LENGTH_SHORT).show();
        Log.i("MonitoraRedeReceiver", "onReceive: " + "mudança");
    }
}
