package com.rtstudio.projetomeuapp.preferencias

import android.content.Context
import com.rtstudio.projetomeuapp.R

class PreferenciasUsuario {
    companion object {
        private val PREF_NAME = "PREFS"
        const val TEMA_PADRAO = "temaPadrao"
        const val TEMA_NOTURNO = "temaNoturno"

        fun setPreferenciaTema(context: Context, tema: String) {
            val preferencia = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()

            preferencia.putString("PREFERENCIA_TEMA", tema)
            preferencia.apply()
        }

        fun getPreferenciaTema(context: Context): String {
            val retorno = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString("PREFERENCIA_TEMA", TEMA_PADRAO)
            return retorno ?: "nao encontrado"
        }

        fun setTema(context: Context) {
            if (getPreferenciaTema(context).equals(TEMA_NOTURNO)) {
                context.setTheme(R.style.TemaModoNotuno)
            } else {
                context.setTheme(R.style.AppTheme)
            }
        }

        fun setPreferenciaLogin(context: Context, nome: String, email: String, isLembra: Boolean) {
            val preferenciaLogin = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()

            preferenciaLogin.putString("NOME", nome)
            preferenciaLogin.putString("EMAIL", email)
            preferenciaLogin.putBoolean("LEMBRAR", isLembra)
            preferenciaLogin.apply()
        }

        fun removePreferenciaLogin(context: Context, isLembra: Boolean) {
            val preferenciaLogin = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()

            preferenciaLogin.remove("NOME")
            preferenciaLogin.remove("EMAIL")
            preferenciaLogin.putBoolean("LEMBRAR", isLembra)
            preferenciaLogin.apply()
        }

        fun getPreferenciaLogin(context: Context): Boolean {
            return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getBoolean("LEMBRAR", false)
        }

        fun getNomeLogin(context: Context): String {
            val retorno = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString("NOME", "")
            return retorno ?: "nao encontrado"

        }

        fun getEmailLogin(context: Context): String {
            val retorno = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString("EMAIL", "")
            return retorno ?: "nao encontrado"

        }
    }
}