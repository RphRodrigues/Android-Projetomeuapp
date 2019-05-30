package com.rtstudio.projetomeuapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rtstudio.projetomeuapp.connection.Connection;
import com.rtstudio.projetomeuapp.modelo.Usuario;

import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    private static final String TABELA_USUARIO = "TABELA_USUARIO";
    private static final String CAMPOS = "ID, PRIMEIRO_NOME, EMAIL, TELEFONE";

    private Context context;

    public UsuarioDAO(Context context) {
        this.context = context;
    }

    public static void createTable(SQLiteDatabase sqLite) {
        StringBuffer create = new StringBuffer();
        create.append("CREATE TABLE IF NOT EXISTS " + TABELA_USUARIO);
        create.append("(");
        create.append("     ID              INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,");
        create.append("     PRIMEIRO_NOME   VARCHAR(100),");
        create.append("     EMAIL           TEXT,");
        create.append("     TELEFONE        TEXT");
        create.append(")");
        sqLite.execSQL(create.toString());
    }

    public long insertUsuario(Usuario usuario) {
        ContentValues values = new ContentValues();
        values.put("PRIMEIRO_NOME", usuario.getNome());
        values.put("EMAIL", usuario.getEmail());
        values.put("TELEFONE", usuario.getTelefone());

        SQLiteDatabase banco = Connection.getInstance(context).getWritableDatabase();

        return banco.insert(TABELA_USUARIO, null, values);
    }

    public List<Usuario> getAllUsuarios() {
        String select = String.format("SELECT %s FROM %s", CAMPOS, TABELA_USUARIO);

        SQLiteDatabase banco = Connection.getInstance(context).getReadableDatabase();

        Cursor cursor = banco.rawQuery(select, null);

        List<Usuario> usuarios = new ArrayList<>();

        try {
            if (cursor.moveToFirst()) {
                do {
                    Usuario usuario = new Usuario();
                    usuario.setUsuarioId(cursor.getInt(cursor.getColumnIndex("ID")));
                    usuario.setNome(cursor.getString(cursor.getColumnIndex("PRIMEIRO_NOME")));
                    usuario.setEmail(cursor.getString(cursor.getColumnIndex("EMAIL")));
                    usuario.setTelefone(cursor.getString(cursor.getColumnIndex("TELEFONE")));
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
        }

        return usuarios;
    }

    public Usuario getUsuarioByEmail(String email) {
        SQLiteDatabase banco = Connection.getInstance(context).getReadableDatabase();

        String select = String.format("SELECT %s FROM %s WHERE EMAIL = ?", CAMPOS, TABELA_USUARIO);

        Cursor cursor = banco.rawQuery(select, new String[]{email});

        Usuario usuario = null;

        if (cursor.moveToFirst()) {
            usuario = new Usuario();
            usuario.setUsuarioId(cursor.getInt(cursor.getColumnIndex("ID")));
            usuario.setNome(cursor.getString(cursor.getColumnIndex("PRIMEIRO_NOME")));
            usuario.setEmail(cursor.getString(cursor.getColumnIndex("EMAIL")));
            usuario.setTelefone(cursor.getString(cursor.getColumnIndex("TELEFONE")));
        }
        cursor.close();

        return usuario;
    }

    public Usuario getUsuarioById(int usuarioId) {
        SQLiteDatabase banco = Connection.getInstance(context).getReadableDatabase();

        String select = String.format("SELECT %s FROM %s WHERE ID = ?", CAMPOS, TABELA_USUARIO);

        Cursor cursor = banco.rawQuery(select, new String[]{String.valueOf(usuarioId)});

        Usuario usuario = new Usuario();

        if (cursor.moveToFirst()) {
            usuario.setUsuarioId(cursor.getInt(cursor.getColumnIndex("ID")));
            usuario.setNome(cursor.getString(cursor.getColumnIndex("PRIMEIRO_NOME")));
            usuario.setEmail(cursor.getString(cursor.getColumnIndex("EMAIL")));
            usuario.setTelefone(cursor.getString(cursor.getColumnIndex("TELEFONE")));
        }
        cursor.close();

        return usuario;
    }

    public boolean deleteUsuario(int usuarioId) {
        SQLiteDatabase banco = Connection.getInstance(context).getWritableDatabase();

        String[] value = new String[]{String.valueOf(usuarioId)};

        return banco.delete(TABELA_USUARIO, "ID = ?", value) > 0;
    }

    public boolean updateUsuario(Usuario usuario) {
        SQLiteDatabase banco = Connection.getInstance(context).getWritableDatabase();

        ContentValues valuesCliente = new ContentValues();
        valuesCliente.put("PRIMEIRO_NOME", usuario.getNome());
        valuesCliente.put("EMAIL", usuario.getEmail());
        valuesCliente.put("TELEFONE", usuario.getTelefone());

        String[] args = new String[]{String.valueOf(usuario.getUsuarioId())};

        return banco.update(TABELA_USUARIO, valuesCliente, "ID = ?", args) > 0;
    }
}