package com.rtstudio.projetomeuapp.classes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.rtstudio.projetomeuapp.R;
import com.rtstudio.projetomeuapp.modelo.Cliente;
import com.rtstudio.projetomeuapp.modelo.Endereco;
import com.rtstudio.projetomeuapp.modelo.OrdemServico;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Raphael Rodrigues on 29/04/2019.
 */
public class Utilitaria {
    private Activity mActivity;

    public Utilitaria(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void desbloquearBotaoSalvar(boolean isDesbloquear) {
        Button button = mActivity.findViewById(R.id.cadastrar_btnCriarOSId);
        if (isDesbloquear) {
            button.setEnabled(true);
            button.setAlpha(1);
        } else {
            button.setEnabled(false);
            button.setAlpha(.5f);
        }
    }

    public void limparCampos(int... ids) {
        if (ids != null) {
            for (int id : ids) {
                setCampos(id, "");
            }
        }
        ((Spinner) mActivity.findViewById(R.id.cadastrar_spinnerEstados)).setSelection(18);
        ((Spinner) mActivity.findViewById(R.id.cadastrar_spinnerTipoServico)).setSelection(0);
    }

    public void setCampos(int id, String data) {
        if (id == R.id.cadastrar_edtComplementoId) {
            ((EditText) mActivity.findViewById(id)).setText(data);
        } else {
            ((TextInputLayout) mActivity.findViewById(id)).getEditText().setText(data);
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

    private void setSpinner(int id, int arrayId, String data) {
        String[] arraySpinner = mActivity.getResources().getStringArray(arrayId);

        for (int i = 0; i < arraySpinner.length; i++) {
            if (data.equals(arraySpinner[i])) {
                ((Spinner) mActivity.findViewById(id)).setSelection(i);
                return;
            }
        }
        ((Spinner) mActivity.findViewById(id)).setSelection(0);
    }

    public void setDadosOrdemServico(OrdemServico os) {
        setDadosCliente(os.getCliente());
        setDadosEndereco(os.getEndereco());
        setSpinner(R.id.cadastrar_spinnerTipoServico, R.array.lista_servico, os.getTipoServico());
        ((TextView) mActivity.findViewById(R.id.editar_tvNumOs)).append(" " + os.getOrdemServicoId());
    }

    public Cliente createCliente(String nomeCliente) {
        return new Cliente(nomeCliente, nomeCliente.substring(0, 3));
    }

    public void menuItemAjuda() {
        String siteAjuda = "http://www.sinapseinformatica.com.br/";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(siteAjuda));
        mActivity.startActivity(intent);
    }

    public void alertDialog(String titulo, String mensagem, boolean cancelable) {
        new AlertDialog.Builder(mActivity)
                .setTitle(titulo)
                .setMessage(mensagem)
                .setCancelable(cancelable)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mActivity.finish();
                    }
                })
                .create()
                .show();
    }

    public void executarSom() {
        MediaPlayer.create(mActivity, R.raw.window_xp_erro).start();
    }

    public void toast(String messagem, int duracao) {
        Toast toast = Toast.makeText(mActivity, messagem, duracao);

        TextView textView = toast.getView().findViewById(android.R.id.message);

        textView.setTextColor(mActivity.getColor(R.color.white));

        toast.getView().getBackground().setColorFilter(mActivity.getColor(R.color.myBlue), PorterDuff.Mode.SRC_IN);

        toast.show();
    }

    public Drawable getWhiteArrow() {
        return mActivity.getResources().getDrawable(R.drawable.ic_arrow_white_24dp, mActivity.getTheme());
    }

    public boolean checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
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
                if (((TextInputLayout) mActivity.findViewById(id)).getEditText().getText().toString().trim().isEmpty()) {
                    ((TextInputLayout) mActivity.findViewById(id)).setError("Digite o nome do cliente");
                    return false;
                } else {
                    ((TextInputLayout) mActivity.findViewById(id)).setError(null);
                    return true;
                }
            case R.id.cadastrar_edtRuaId:
                if (((TextInputLayout) mActivity.findViewById(id)).getEditText().getText().toString().trim().isEmpty()) {
                    ((TextInputLayout) mActivity.findViewById(id)).setError("Digite o nome da rua");
                    return false;
                } else {
                    ((TextInputLayout) mActivity.findViewById(id)).setError(null);
                    return true;
                }
            case R.id.cadastrar_edtBairroId:
                if (((TextInputLayout) mActivity.findViewById(id)).getEditText().getText().toString().trim().isEmpty()) {
                    ((TextInputLayout) mActivity.findViewById(id)).setError("Informe o bairro");
                    return false;
                } else {
                    ((TextInputLayout) mActivity.findViewById(id)).setError(null);
                    return true;
                }
            case R.id.cadastrar_edtCepId:
                if (((TextInputLayout) mActivity.findViewById(id)).getEditText().getText().toString().trim().isEmpty()) {
                    ((TextInputLayout) mActivity.findViewById(id)).setError("Digite o cep");
                    return false;
                } else {
                    ((TextInputLayout) mActivity.findViewById(id)).setError(null);
                    return true;
                }
            case R.id.cadastrar_edtNumeroId:
                if (((TextInputLayout) mActivity.findViewById(id)).getEditText().getText().toString().trim().isEmpty()) {
                    ((TextInputLayout) mActivity.findViewById(id)).setError("Digite o número");
                    return false;
                } else {
                    ((TextInputLayout) mActivity.findViewById(id)).setError(null);
                    return true;
                }
            case R.id.cadastrar_edtCidadeId:
                if (((TextInputLayout) mActivity.findViewById(id)).getEditText().getText().toString().trim().isEmpty()) {
                    ((TextInputLayout) mActivity.findViewById(id)).setError("Informe a cidade");
                    return false;
                } else {
                    ((TextInputLayout) mActivity.findViewById(id)).setError(null);
                    return true;
                }
            case R.id.fragment_editar_produto:
                if (((TextInputLayout) mActivity.findViewById(id)).getEditText().getText().toString().trim().isEmpty()) {
                    ((TextInputLayout) mActivity.findViewById(id)).setError("Informe o produto");
                    return false;
                } else {
                    ((TextInputLayout) mActivity.findViewById(id)).setError(null);
                    return true;
                }
        }
        return true;
    }

    @SuppressLint("MissingPermission")
    public void getLocalizacaoGPS() {
        Log.v("GPS", "getLocalizacaoGPS");

        LocationManager locationManager = (LocationManager) mActivity.getSystemService(LOCATION_SERVICE);

        locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                geoReferenciamento(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        }, null);
    }


    @SuppressLint("MissingPermission")
    public void getLocalizacao() {
        Log.v("GPS", "getLocalizacao");

        LocationManager locationManager = (LocationManager) mActivity.getSystemService(LOCATION_SERVICE);
        boolean ativado = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!ativado) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            mActivity.startActivity(intent);
            return;
        }

        FusedLocationProviderClient flpc = LocationServices.getFusedLocationProviderClient(mActivity.getApplicationContext());

        flpc.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                geoReferenciamento(location);
            }
        });
    }

    private void geoReferenciamento(Location location) {
        Log.v("GPS", "geoReferenciamento");

        Geocoder geo = new Geocoder(mActivity.getApplicationContext(), Locale.getDefault());
        try {
            List<Address> enderecos = geo.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if ((enderecos != null) && (enderecos.size() > 0)) {
                Address address = enderecos.get(0);

                //Rua
                ((TextInputLayout) mActivity.findViewById(R.id.cadastrar_edtRuaId)).getEditText().setText(address.getThoroughfare());

                //Número
                if (address.getFeatureName().contains("-")) {
                    ((TextInputLayout) mActivity.findViewById(R.id.cadastrar_edtNumeroId)).setTag(address.getFeatureName().substring(0, address.getFeatureName().indexOf("-")));
                } else {
                    ((TextInputLayout) mActivity.findViewById(R.id.cadastrar_edtNumeroId)).getEditText().setText(address.getFeatureName());
                }

                //Bairro
                ((TextInputLayout) mActivity.findViewById(R.id.cadastrar_edtBairroId)).getEditText().setText(address.getSubLocality());

                //Cidade
                ((TextInputLayout) mActivity.findViewById(R.id.cadastrar_edtCidadeId)).getEditText().setText(address.getSubAdminArea());

                //Cep
                ((TextInputLayout) mActivity.findViewById(R.id.cadastrar_edtCepId)).getEditText().setText(address.getPostalCode());

                //Estado
                address.getAdminArea();

                //Pais
                address.getCountryName();

                //Código do pais
                address.getCountryCode();

                //Cultura
                address.getLocale();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(mActivity, "Aguardando triangulação do gps", Toast.LENGTH_SHORT).show();
        }
    }
}
