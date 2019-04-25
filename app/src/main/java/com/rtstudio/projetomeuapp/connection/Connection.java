package com.rtstudio.projetomeuapp.connection;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.rtstudio.projetomeuapp.DAO.ClienteDAO;
import com.rtstudio.projetomeuapp.DAO.EnderecoDAO;
import com.rtstudio.projetomeuapp.DAO.OrdemServicoDAO;

public class Connection extends SQLiteOpenHelper {

    private static final String BD_NAME = "BancoDeDadosTcc";
    private static final int BD_VERSION = 1;

    private static Connection connection;

    public Connection(Context context) {
        super(context, BD_NAME, null, BD_VERSION);
    }

    public static Connection getInstance(Context context) {
        if (connection == null) {
            connection = new Connection(context);
        }
        return connection;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        ClienteDAO.createTable(db);
        EnderecoDAO.createTable(db);
        OrdemServicoDAO.createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        if () {
//
//        }
    }
}
