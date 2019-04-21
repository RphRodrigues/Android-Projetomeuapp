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
        create.append("     BAIRRO              TEXT,");
        create.append("     TIPOSERVICO         TEXT,");
        create.append(")");
        sqLite.execSQL(create.toString());
    }

    public boolean insert(OrdemServico os) {
        ContentValues contentValuesOS = new ContentValues();

        ClienteDAO clienteDAO = new ClienteDAO(context);
        if (clienteDAO.insert(os.getCliente()) == -1) {
            return false;
        }

        EnderecoDAO enderecoDAO = new EnderecoDAO(context);
        if (enderecoDAO.insert(os.getEndereco()) == -1) {
            return false;
        }

        contentValuesOS.put("TIPOSERVICO", os.getTipo());

        SQLiteDatabase conn = Connection.getInstance(context).getWritableDatabase();

        conn.beginTransaction();


        conn.endTransaction(); // no final

        return conn.insert(ORDEM_SERVICO, null, contentValuesOS) != -1;
    }

    public List<OrdemServico> getAll() {
        String select = String.format("SELECT %s FROM %s", CAMPOS, ORDEM_SERVICO);

        SQLiteDatabase conn = Connection.getInstance(context).getReadableDatabase();

        Cursor cursor = conn.rawQuery(select, null);

        List<OrdemServico> ordens = new ArrayList<>();

        try {
            if (cursor.moveToFirst()) {
                do {
                    String bairro = cursor.getString(cursor.getColumnIndex("BAIRRO"));
                    String tipoServico = cursor.getString(cursor.getColumnIndex("TIPOSERVICO"));
                    int ordemServicoId = cursor.getInt(cursor.getColumnIndex("ORDEM_SERVICO_ID"));

                    Cliente c = new Cliente();
                    Endereco end = new Endereco();
                    end.setBairro(bairro);
//
//                    OrdemServico os = new OrdemServico(c, end, tipoServico);
//                    os.setOrdemServicoId(ordemServicoId);

//                    ordens.add(os);
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
        }
        return ordens;
    }
}
