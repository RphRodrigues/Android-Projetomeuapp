package com.rtstudio.projetomeuapp.classes;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.design.widget.TextInputLayout;
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

    public void bloquearCampos(boolean isBloquear, int...ids) {
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
        ((Spinner) activity.findViewById(R.id.cadastrar_spinnerEstados)).setSelection(18);
        ((Spinner) activity.findViewById(R.id.cadastrar_spinnerTipoServico)).setSelection(0);
    }

    public void setCampos(int id, String data) {
        if (id == R.id.cadastrar_edtComplementoId) {
            ((EditText) activity.findViewById(id)).setText(data);
        } else {
            ((TextInputLayout) activity.findViewById(id)).getEditText().setText(data);
        }
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

    public boolean checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public boolean validarCampos(int... ids) {

        if (ids.length > 0) {
            boolean[] resultado = new boolean[ids.length];
            for (int i = 0; i < ids.length; i++) {
                resultado[i] = checkCampo(ids[i]);
            }

            for (boolean b : resultado) {
                if (!b) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean checkCampo(int id) {

            switch (id) {
                case R.id.cadastrar_edtNomeClienteId:
                    if (((TextInputLayout) activity.findViewById(id)).getEditText().getText().toString().trim().isEmpty()) {
                        ((TextInputLayout) activity.findViewById(id)).setError("Digite o nome do cliente");
                        return false;
                    } else {
                        ((TextInputLayout) activity.findViewById(id)).setError(null);
                        ((TextInputLayout) activity.findViewById(id)).setErrorEnabled(false);
                        return true;
                    }
                case R.id.cadastrar_edtRuaId:
                    if (((TextInputLayout) activity.findViewById(id)).getEditText().getText().toString().trim().isEmpty()) {
                        ((TextInputLayout) activity.findViewById(id)).setError("Digite o nome da rua");
                        return false;
                    } else {
                        ((TextInputLayout) activity.findViewById(id)).setError(null);
                        ((TextInputLayout) activity.findViewById(id)).setErrorEnabled(false);
                        return true;
                    }
                case R.id.cadastrar_edtBairroId:
                    if (((TextInputLayout) activity.findViewById(id)).getEditText().getText().toString().trim().isEmpty()) {
                        ((TextInputLayout) activity.findViewById(id)).setError("Informe o bairro");
                        return false;
                    } else {
                        ((TextInputLayout) activity.findViewById(id)).setError(null);
                        ((TextInputLayout) activity.findViewById(id)).setErrorEnabled(false);
                        return true;
                    }
                case R.id.cadastrar_edtCepId:
                    if (((TextInputLayout) activity.findViewById(id)).getEditText().getText().toString().trim().isEmpty()) {
                        ((TextInputLayout) activity.findViewById(id)).setError("Digite o cep");
                        return false;
                    } else {
                        ((TextInputLayout) activity.findViewById(id)).setError(null);
                        ((TextInputLayout) activity.findViewById(id)).setErrorEnabled(false);
                        return true;
                    }
                case R.id.cadastrar_edtNumeroId:
                    if (((TextInputLayout) activity.findViewById(id)).getEditText().getText().toString().trim().isEmpty()) {
                        ((TextInputLayout) activity.findViewById(id)).setError("Digite o número");
                        return false;
                    } else {
                        ((TextInputLayout) activity.findViewById(id)).setError(null);
                        ((TextInputLayout) activity.findViewById(id)).setErrorEnabled(false);
                        return true;
                    }
                case R.id.cadastrar_edtCidadeId:
                    if (((TextInputLayout) activity.findViewById(id)).getEditText().getText().toString().trim().isEmpty()) {
                        ((TextInputLayout) activity.findViewById(id)).setError("Informe a cidade");
                        return false;
                    } else {
                        ((TextInputLayout) activity.findViewById(id)).setError(null);
                        ((TextInputLayout) activity.findViewById(id)).setErrorEnabled(false);
                        return true;
                    }
            }
        return true;
    }
}
