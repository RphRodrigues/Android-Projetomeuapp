package com.rtstudio.projetomeuapp.classes;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.rtstudio.projetomeuapp.R;

/**
 * Created by Raphael Rodrigues on 29/04/2019.
 */
public class Utilitaria {
    private Activity activity;
    private int[] ids;

    public Utilitaria(Activity activity) {
        this.activity = activity;
    }

    public Utilitaria(Activity activity, int... ids) {
        this.activity = activity;
        this.ids = ids;
    }

    public void bloquearCampos(boolean isBloquear) {
        for (int id : ids) {
            setBloquearCampos(id, isBloquear);
        }
    }

    private void setBloquearCampos(int id, boolean isBloquear) {
        activity.findViewById(id).setEnabled(!isBloquear);
    }

    public void limparCampos(int... ids) {
        if (ids != null) {
            for (int id : ids) {
                setCampos(id, "");
            }
        }
        ((Spinner) activity.findViewById(R.id.cadastrar_spinnerEstados)).setSelection(0);
        ((Spinner) activity.findViewById(R.id.cadastrar_spinnerTipoServico)).setSelection(0);
    }

    public void setCampos(int id, String data) {
        ((EditText) activity.findViewById(id)).setText(data);
    }

    public void setDadosCliente(Cliente cliente) {
        setCampos(R.id.cadastrar_edtNomeClienteId, cliente.getNome());
    }

    public void setDadosEndereco(Endereco endereco) {
        setCampos(R.id.cadastrar_edtCepId, endereco.getCep());
        setCampos(R.id.cadastrar_edtRuaId, endereco.getLogradouro());
        setCampos(R.id.cadastrar_edtNumeroId, endereco.getNumero());
        setCampos(R.id.cadastrar_edtBairroId, endereco.getBairro());
        setCampos(R.id.cadastrar_edtCidadeId, endereco.getLocalidade());
        setCampos(R.id.cadastrar_edtComplementoId, endereco.getComplemento());
        setSpinner(R.id.cadastrar_spinnerEstados, R.array.estados, endereco.getUf());
    }

    public void setSpinner(int id, int arrayId, String data) {
        String[] arraySpinner = activity.getResources().getStringArray(arrayId);

        for (int i = 0; i < arraySpinner.length; i++) {
            if (data.equals(arraySpinner[i])) {
                ((Spinner) activity.findViewById(id)).setSelection(i);
                return;
            }
        }
        ((Spinner) activity.findViewById(id)).setSelection(0);
    }

    public void setDadosOrdemServico(OrdemServico os) {
        setDadosCliente(os.getCliente());
        setDadosEndereco(os.getEndereco());
        setSpinner(R.id.cadastrar_spinnerTipoServico, R.array.lista_servico, os.getTipoServico());
        ((EditText) activity.findViewById(R.id.cadastrar_edtDescricaoServicosId)).setText(os.getDescricaoServico());
        ((TextView) activity.findViewById(R.id.editar_tvNumOs)).append(" " + os.getOrdemServicoId());
    }

    public Cliente createCliente(String nomeCliente) {
        return new Cliente(nomeCliente, nomeCliente.substring(0, 3));
    }

    public void menuItemAjuda() {
        String siteAjuda = "http://www.sinapseinformatica.com.br/";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(siteAjuda));
        activity.startActivity(intent);
    }

    public void alertDialog(String titulo, String mensagem, boolean cancelable) {
        new AlertDialog.Builder(activity)
                .setTitle(titulo)
                .setMessage(mensagem)
                .setCancelable(cancelable)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activity.finish();
                    }
                })
                .create()
                .show();
    }
}
