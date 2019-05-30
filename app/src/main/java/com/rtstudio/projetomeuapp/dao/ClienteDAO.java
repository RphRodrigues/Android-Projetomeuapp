package com.rtstudio.projetomeuapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rtstudio.projetomeuapp.modelo.Cliente;
import com.rtstudio.projetomeuapp.connection.Connection;

public class ClienteDAO {
    private static final String TABELA_CLIENTE = "TABELA_CLIENTE";
    private static final String CAMPOS = "ID, NOME_CLIENTE, COD_CLIENTE";

    private Context context;

    public ClienteDAO(Context context) {
        this.context = context;
    }

    public static void createTable(SQLiteDatabase sqLite) {
        StringBuffer create = new StringBuffer();
        create.append("CREATE TABLE IF NOT EXISTS TABELA_CLIENTE");
        create.append("(");
        create.append("     ID              INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,");
        create.append("     NOME_CLIENTE    TEXT,");
        create.append("     COD_CLIENTE     TEXT");
        create.append(")");
        sqLite.execSQL(create.toString());
    }

    public long insertCliente(Cliente cliente) {
        ContentValues values = new ContentValues();
        values.put("NOME_CLIENTE", cliente.getNome());
        values.put("COD_CLIENTE", cliente.getCodigoCliente());

        SQLiteDatabase banco = Connection.getInstance(context).getWritableDatabase();

        return banco.insert(TABELA_CLIENTE, null, values);
    }


    public Cliente getClienteById(int clienteId) {
        SQLiteDatabase banco = Connection.getInstance(context).getReadableDatabase();

        String select = String.format("SELECT %s FROM %s WHERE ID = ?", CAMPOS, TABELA_CLIENTE);

        Cursor cursor = banco.rawQuery(select, new String[]{String.valueOf(clienteId)});

        Cliente cliente = new Cliente();

        if (cursor.moveToFirst()) {
            cliente.setClienteId(cursor.getInt(cursor.getColumnIndex("ID")));
            cliente.setNome(cursor.getString(cursor.getColumnIndex("NOME_CLIENTE")));
            cliente.setCodigoCliente(cursor.getString(cursor.getColumnIndex("COD_CLIENTE")));
        }
        cursor.close();

        return cliente;
    }

    public boolean deleteCliente(int clienteId) {
        SQLiteDatabase banco = Connection.getInstance(context).getWritableDatabase();

        String[] value = new String[]{String.valueOf(clienteId)};

        return banco.delete(TABELA_CLIENTE, "ID = ?", value) > 0;
    }


    public boolean updateCliente(Cliente cliente) {
        SQLiteDatabase banco = Connection.getInstance(context).getWritableDatabase();

        ContentValues valuesCliente = new ContentValues();
        valuesCliente.put("NOME_CLIENTE", cliente.getNome());
        valuesCliente.put("COD_CLIENTE", cliente.getCodigoCliente());

        String[] args = new String[]{String.valueOf(cliente.getClienteId())};

        return banco.update(TABELA_CLIENTE, valuesCliente, "ID = ?", args) > 0;
    }
}
