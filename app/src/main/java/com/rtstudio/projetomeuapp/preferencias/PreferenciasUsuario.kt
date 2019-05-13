package com.rtstudio.projetomeuapp.preferencias

import android.content.Context
import com.rtstudio.projetomeuapp.R
import com.rtstudio.projetomeuapp.TelaInicialActivity

class PreferenciasUsuario {
    companion object {
        val TEMA = "TEMA"

        fun setPreferenciaTema(context: Context, tema: String) {
            val preferencia = context.getSharedPreferences(TEMA, Context.MODE_PRIVATE).edit()

            preferencia.putString("PREFERENCIA_TEMA", tema)
            preferencia.apply()
        }

        fun getPreferenciaTema(context: Context): String {
            val retorno = context.getSharedPreferences(TEMA, Context.MODE_PRIVATE).getString("PREFERENCIA_TEMA", TelaInicialActivity.TEMA_PADRAO)
            return retorno ?: "nao encontrado"
        }

        fun setTema(context: Context) {
            if (getPreferenciaTema(context).equals(TelaInicialActivity.TEMA_NOTURNO)) {
                context.setTheme(R.style.TemaModoNotuno)
            } else {
                context.setTheme(R.style.AppTheme)
            }
        }
    }
}