package com.rtstudio.projetomeuapp.preferencias;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenciasUsuario {
    public static final String COR = "COR_SharedPreferenceApp";

    public static void setCor(Context context, String cor) {
        SharedPreferences.Editor preferencia = context.getSharedPreferences(COR, Context.MODE_PRIVATE).edit();

        preferencia.putString("PREFERENCIA_COR", cor);
        preferencia.apply();
    }

    public static String getCor(Context context) {
        return context.getSharedPreferences(COR, Context.MODE_PRIVATE).getString("PREFERENCIA_COR", "AZUL");
    }
}
