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
    private final String TABELA_ORDEM_SERVICO = "TABELA_ORDEM_SERVICO";
    private final String CAMPOS = "ORDEM_SERVICO_ID, CLIENTE_ID, ENDERECO_ID, TIPOSERVICO";

    private Context context;

    public OrdemServicoDAO(Context context) {
        this.context = context;
    }

    public static void createTable(SQLiteDatabase sqLite) {
        StringBuffer create = new StringBuffer();
        create.append("CREATE TABLE IF NOT EXISTS TABELA_ORDEM_SERVICO");
        create.append("(");
        create.append("     ORDEM_SERVICO_ID    INTEGER NOT NULL PRIMARY KEY,");
        create.append("     CLIENTE_ID          INTEGER,");
        create.append("     ENDERECO_ID         INTEGER,");
        create.append("     TIPOSERVICO         TEXT");
        create.append(")");
        sqLite.execSQL(create.toString());
    }

    public boolean insert(OrdemServico os) {
        SQLiteDatabase banco;
        ContentValues values = new ContentValues();

        ClienteDAO clienteDAO = new ClienteDAO(context);
        long clienteId = clienteDAO.insert(os.getCliente());
        if (clienteId == -1) {
            return false;
        }

        EnderecoDAO enderecoDAO = new EnderecoDAO(context);
        long enderecoId = enderecoDAO.insert(os.getEndereco());
        if (enderecoId == -1) {
            return false;
        }

        values.put("ORDEM_SERVICO_ID", os.getOrdemServicoId());
        values.put("CLIENTE_ID", clienteId);
        values.put("ENDERECO_ID", enderecoId);
        values.put("TIPOSERVICO", os.getTipoServico());

        banco = Connection.getInstance(context).getWritableDatabase();

        banco.beginTransaction();

        long ordemServicoId;
        try {
            ordemServicoId = banco.insert(TABELA_ORDEM_SERVICO, null, values);
            banco.setTransactionSuccessful();
        } finally {
            banco.endTransaction();
        }

        return ordemServicoId != -1;
    }

    public List<OrdemServico> getAll() {
        String select = String.format("SELECT %s FROM %s", CAMPOS, TABELA_ORDEM_SERVICO);

        SQLiteDatabase banco = Connection.getInstance(context).getReadableDatabase();

        Cursor cursor = banco.rawQuery(select, null);

        List<OrdemServico> ordens = new ArrayList<>();

        try {
            if (cursor.moveToFirst()) {
                do {
                    int ordemServicoId = cursor.getInt(cursor.getColumnIndex("ORDEM_SERVICO_ID"));
                    int clienteId = cursor.getInt(cursor.getColumnIndex("CLIENTE_ID"));
                    int enderecoId = cursor.getInt(cursor.getColumnIndex("ENDERECO_ID"));
                    String tipoServico = cursor.getString(cursor.getColumnIndex("TIPOSERVICO"));

                    Cliente cliente = new ClienteDAO(context).getClienteById(clienteId);
                    Endereco endereco = new EnderecoDAO(context).getEnderecoById(enderecoId);

                    OrdemServico os = new OrdemServico(ordemServicoId, cliente, endereco, tipoServico);

                    ordens.add(os);
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
        }
        return ordens;
    }
}
