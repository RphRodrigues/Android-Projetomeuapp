package com.rtstudio.projetomeuapp.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.rtstudio.projetomeuapp.classes.Cliente;
import com.rtstudio.projetomeuapp.connection.Connection;

public class  ClienteDAO {
    static final String TABELA_CLIENTE = "TABELA_CLIENTE";
    final String CAMPOS = "ID, NOME, COD_CLIENTE";

    private Context context;

    public ClienteDAO(Context context) {
        this.context = context;
    }

    public static void createTable(SQLiteDatabase sqLite) {
        StringBuffer create = new StringBuffer();
        create.append("CREATE TABLE IF NOT EXISTS TABELA_CLIENTE");
        create.append("(");
        create.append("     ID              INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,");
        create.append("     NOME            TEXT,");
        create.append("     COD_CLIENTE     TEXT,");
        create.append(")");
        sqLite.execSQL(create.toString());
    }

    public long insert(Cliente cliente) {
        ContentValues contentValuesCliente = new ContentValues();
        contentValuesCliente.put("NOME", cliente.getNome());
        contentValuesCliente.put("COD_CLIENTE", cliente.getCodigoCliente());

        SQLiteDatabase conn = Connection.getInstance(context).getWritableDatabase();

        return conn.insert(TABELA_CLIENTE, null, contentValuesCliente);

    }

}
