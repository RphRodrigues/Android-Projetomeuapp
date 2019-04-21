package com.rtstudio.projetomeuapp.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.rtstudio.projetomeuapp.classes.Endereco;
import com.rtstudio.projetomeuapp.connection.Connection;

public class EnderecoDAO {
    private final String TABELA_ENDERECO = "TABELA_ENDERECO";
    private final String CAMPOS = "ID, RUA, NUMERO, BAIRRO, CEP, CIDADE, ESTADO";

    private Context context;

    public EnderecoDAO(Context context) {
        this.context = context;
    }

    public static void createTable(SQLiteDatabase sqLite) {
        StringBuffer create = new StringBuffer();
        create.append("CREATE TABLE ID NOT EXISTS TABELA_ENDERECO");
        create.append("(");
        create.append("     ID          INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT");
        create.append("     RUA         NVARCHAR(5),");
        create.append("     NUMERO      TEXT,");
        create.append("     BAIRRO      TEXT,");
        create.append("     CEP         TEXT,");
        create.append("     CIDADE      TEXT,");
        create.append("     ESTADO      TEXT,");
        create.append(")");
        sqLite.execSQL(create.toString());
    }

    public long insert(Endereco endereco) {
        ContentValues contentValuesEndereco = new ContentValues();
        contentValuesEndereco.put("RUA", endereco.getRua());
        contentValuesEndereco.put("NUMERO", endereco.getNumero());
        contentValuesEndereco.put("BAIRRO", endereco.getBairro());
        contentValuesEndereco.put("CEP",    endereco.getCep());
        contentValuesEndereco.put("CIDADE", endereco.getCidade());
        contentValuesEndereco.put("ESTADO", endereco.getEstado());

        SQLiteDatabase conn = Connection.getInstance(context).getWritableDatabase();

        return conn.insert(TABELA_ENDERECO, null, contentValuesEndereco);
    }
}
