package com.rtstudio.projetomeuapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rtstudio.projetomeuapp.modelo.Endereco;
import com.rtstudio.projetomeuapp.connection.Connection;

public class EnderecoDAO {
    public static final String TABELA_ENDERECO = "TABELA_ENDERECO";
    public static final String CAMPOS = "ID, RUA, NUMERO, BAIRRO, CEP, CIDADE, ESTADO, COMPLEMENTO";

    private Context context;

    public EnderecoDAO(Context context) {
        this.context = context;
    }

    public static void createTable(SQLiteDatabase sqLite) {
        StringBuffer create = new StringBuffer();
        create.append("CREATE TABLE IF NOT EXISTS TABELA_ENDERECO");
        create.append("(");
        create.append("     ID              INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,");
        create.append("     RUA             VARCHAR(100),");
        create.append("     NUMERO          TEXT,");
        create.append("     BAIRRO          TEXT,");
        create.append("     CEP             TEXT,");
        create.append("     CIDADE          TEXT,");
        create.append("     ESTADO          TEXT,");
        create.append("     COMPLEMENTO     TEXT");
        create.append(")");
        sqLite.execSQL(create.toString());
    }

    public long insertEndereco(Endereco endereco) {
        ContentValues contentValuesEndereco = new ContentValues();
        contentValuesEndereco.put("RUA", endereco.getLogradouro());
        contentValuesEndereco.put("NUMERO", endereco.getNumero());
        contentValuesEndereco.put("BAIRRO", endereco.getBairro());
        contentValuesEndereco.put("CEP", endereco.getCep());
        contentValuesEndereco.put("CIDADE", endereco.getLocalidade());
        contentValuesEndereco.put("ESTADO", endereco.getUf());
        contentValuesEndereco.put("COMPLEMENTO", endereco.getComplemento());

        SQLiteDatabase conn = Connection.getInstance(context).getWritableDatabase();

        return conn.insert(TABELA_ENDERECO, null, contentValuesEndereco);
    }

    public Endereco getEnderecoById(int enderecoId) {
        SQLiteDatabase banco = Connection.getInstance(context).getReadableDatabase();

        String select = String.format("SELECT %s FROM %s WHERE ID = ?", CAMPOS, TABELA_ENDERECO);

        Cursor cursor = banco.rawQuery(select, new String[]{String.valueOf(enderecoId)});

        Endereco endereco = new Endereco();

        if (cursor.moveToFirst()) {
            endereco.setEnderecoId(cursor.getInt(cursor.getColumnIndex("ID")));
            endereco.setLogradouro(cursor.getString(cursor.getColumnIndex("RUA")));
            endereco.setNumero(cursor.getString(cursor.getColumnIndex("NUMERO")));
            endereco.setLocalidade(cursor.getString(cursor.getColumnIndex("CIDADE")));
            endereco.setCep(cursor.getString(cursor.getColumnIndex("CEP")));
            endereco.setUf(cursor.getString(cursor.getColumnIndex("ESTADO")));
            endereco.setBairro(cursor.getString(cursor.getColumnIndex("BAIRRO")));
            endereco.setComplemento(cursor.getString(cursor.getColumnIndex("COMPLEMENTO")));
        }
        cursor.close();

        return endereco;
    }

    public boolean deleteEndereco(int enderecoId) {
        SQLiteDatabase banco = Connection.getInstance(context).getWritableDatabase();

        String[] value = new String[]{String.valueOf(enderecoId)};

        return banco.delete(TABELA_ENDERECO, "ID = ?", value) > 0;
    }

    public boolean updateEndereco(Endereco endereco) {
        SQLiteDatabase banco = Connection.getInstance(context).getWritableDatabase();

        ContentValues valuesEndereco = new ContentValues();
        valuesEndereco.put("RUA", endereco.getLogradouro());
        valuesEndereco.put("NUMERO", endereco.getNumero());
        valuesEndereco.put("BAIRRO", endereco.getBairro());
        valuesEndereco.put("CEP", endereco.getCep());
        valuesEndereco.put("CIDADE", endereco.getLocalidade());
        valuesEndereco.put("ESTADO", endereco.getUf());
        valuesEndereco.put("COMPLEMENTO", endereco.getComplemento());

        String[] args = new String[]{String.valueOf(endereco.getEnderecoId())};

        return banco.update(TABELA_ENDERECO, valuesEndereco, "ID = ?", args) > 0;
    }
}
