package com.rtstudio.projetomeuapp.preferencias;

import android.content.Context;
import android.content.SharedPreferences;

import com.rtstudio.projetomeuapp.R;
import com.rtstudio.projetomeuapp.TelaInicialActivity;


public class PreferenciasUsuario {
    public static final String TEMA = "TEMA_SharedPreferenceApp";

    public static void setPreferenciaTema(Context context, String tema) {
        SharedPreferences.Editor preferencia = context.getSharedPreferences(TEMA, Context.MODE_PRIVATE).edit();

        preferencia.putString("PREFERENCIA_TEMA", tema);
        preferencia.apply();
    }

    public static String getPreferenciaTema(Context context) {
        return context.getSharedPreferences(TEMA, Context.MODE_PRIVATE).getString("PREFERENCIA_TEMA", TelaInicialActivity.TEMA_PADRAO);
    }

    public static void setTema(Context context) {
        if (getPreferenciaTema(context).equals(TelaInicialActivity.TEMA_NOTURNO)) {
            context.setTheme(R.style.TemaModoNotuno);
        } else {
            context.setTheme(R.style.AppTheme);
        }
    }
}
