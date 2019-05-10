package com.rtstudio.projetomeuapp.server;

import android.os.AsyncTask;

/**
 * Created by Raphael Rodrigues on 10/05/2019.
 */
public class WebServiceDelete extends AsyncTask<Integer, Void, Void> {

    @Override
    protected Void doInBackground(Integer... ids) {
        ConnectServer.delete(ids[0]);
        return null;
    }
}
