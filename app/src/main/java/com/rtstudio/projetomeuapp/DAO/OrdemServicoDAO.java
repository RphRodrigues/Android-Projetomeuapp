package com.rtstudio.projetomeuapp.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rtstudio.projetomeuapp.classes.Cliente;
import com.rtstudio.projetomeuapp.classes.Endereco;
import com.rtstudio.projetomeuapp.classes.OrdemServico;
import com.rtstudio.projetomeuapp.connection.Connection;

import java.util.ArrayList;
import java.util.List;

public class OrdemServicoDAO {
    private final String ORDEM_SERVICO = "TABELA_ORDEM_SERVICO";
    private final String CAMPOS = "ID, ORDEM_SERVICO_ID, CLIENTE, ENDERECO, TIPOSERVICO";

    private Context context;

    public OrdemServicoDAO(Context context) {
        this.context = context;
    }

    public static void createTable(SQLiteDatabase sqLite) {
        StringBuffer create = new StringBuffer();
        create.append("CREATE TABLE IF NOT EXISTS TABELA_ORDEM_SERVICO");
        create.append("(");
        create.append("     ID                  INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,");
        create.append("     ORDEM_SERVICO_ID    INTEGER,");
        create.append("     CLIENTE             INTEGER,");
        create.append("     ENDERECO            INTEGER,");
        create.append("     TIPOSERVICO         TEXT");
        create.append(")");
        sqLite.execSQL(create.toString());
    }

    public boolean insert(OrdemServico os) {
        SQLiteDatabase banco;
        ContentValues values = new ContentValues();

        ClienteDAO clienteDAO = new ClienteDAO(context);
        long idCliente = clienteDAO.insert(os.getCliente());
        if (idCliente == -1) {
            return false;
        }

        EnderecoDAO enderecoDAO = new EnderecoDAO(context);
        long idEndereco = enderecoDAO.insert(os.getEndereco());
        if (idEndereco == -1) {
            return false;
        }

        values.put("ORDEM_SERVICO_ID", os.getOrdemServicoId());
        values.put("CLIENTE", idCliente);
        values.put("ENDERECO", idEndereco);
        values.put("TIPOSERVICO", os.getTipoServico());

        banco = Connection.getInstance(context).getWritableDatabase();

        banco.beginTransaction();

        long idOrdemServico;
        try {
            idOrdemServico = banco.insert(ORDEM_SERVICO, null, values);
            banco.setTransactionSuccessful();
        } finally {
            banco.endTransaction(); // no final
        }

        return idOrdemServico != -1;
    }

    public List<OrdemServico> getAll() {
        String select = String.format("SELECT %s FROM %s", CAMPOS, ORDEM_SERVICO);

        SQLiteDatabase banco = Connection.getInstance(context).getReadableDatabase();

        Cursor cursor = banco.rawQuery(select, null);

        List<OrdemServico> ordens = new ArrayList<>();

        try {
            if (cursor.moveToFirst()) {
                do {
                    int idOrdemServico = cursor.getInt(cursor.getColumnIndex("ORDEM_SERVICO_ID"));
                    int idCliente = cursor.getInt(cursor.getColumnIndex("CLIENTE"));
                    int idEndereco = cursor.getInt(cursor.getColumnIndex("ENDERECO"));
                    String tipoServico = cursor.getString(cursor.getColumnIndex("TIPOSERVICO"));

                    Cliente cliente = new ClienteDAO(context).getClienteById(idCliente);
                    Endereco endereco = new EnderecoDAO(context).getEnderecoById(idEndereco);

                    OrdemServico os = new OrdemServico(idOrdemServico, cliente, endereco, tipoServico);

                    ordens.add(os);
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
        }
        return ordens;
    }
}
