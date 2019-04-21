package com.rtstudio.projetomeuapp.connection;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.rtstudio.projetomeuapp.DAO.OrdemServicoDAO;

public class Connection extends SQLiteOpenHelper {

    private static String BD_NAME = "projeto";
    private static int BD_VERSION = 1;

    private static Connection data;

    public Connection(Context context) {
        super(context, BD_NAME, null, BD_VERSION);
    }

    public static Connection getInstance(Context context) {
        if (data == null) {
            data = new Connection(context);
        }
        return data;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        OrdemServicoDAO.createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        if () {
//
//        }
    }
}
