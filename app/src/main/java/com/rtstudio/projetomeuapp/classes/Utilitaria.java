package com.rtstudio.projetomeuapp.classes;

import android.app.Activity;

/**
 * Created by Raphael Rodrigues on 29/04/2019.
 */
public class Utilitaria {
    private Activity activity;
    private int[] ids;

    public Utilitaria(Activity activity, int... ids) {
        this.activity = activity;
        this.ids = ids;
    }

    public void bloquearCampos(boolean isBloquear) {
        for (int id : ids) {
            setBloquearCampor(id, isBloquear);
        }
    }

    private void setBloquearCampor(int id, boolean isBloquear) {
        activity.findViewById(id).setEnabled(!isBloquear);
    }
}
