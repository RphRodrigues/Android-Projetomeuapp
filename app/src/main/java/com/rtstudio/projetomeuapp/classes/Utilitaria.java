package com.rtstudio.projetomeuapp.classes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.rtstudio.projetomeuapp.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Raphael Rodrigues on 29/04/2019.
 */
public class Utilitaria {
    private Activity activity;
    private Fragment fragment;
    private View mView;

    public Utilitaria(Fragment fragment, View view) {
        this.fragment = fragment;
        this.mView = view;
    }

    public Utilitaria(Activity activity) {
        this.activity = activity;
    }

    public Utilitaria(Fragment fragment) {
        this.fragment = fragment;
    }

    public void bloquearCampos(boolean isBloquear) {
        int[] ids = {R.id.cadastrar_edtRuaId, R.id.cadastrar_edtCepId, R.id.cadastrar_edtComplementoId,
                R.id.cadastrar_edtBairroId, R.id.cadastrar_edtCidadeId, R.id.cadastrar_edtNumeroId};

        for (int id : ids) {
            setBloquearCampos(id, isBloquear);
        }
    }

    private void setBloquearCampos(int id, boolean isBloquear) {
        mView.findViewById(id).setEnabled(!isBloquear);
    }

    public void limparCampos(int... ids) {
        if (ids != null) {
            for (int id : ids) {
                setCampos(id, "");
            }
        }
        ((Spinner) mView.findViewById(R.id.cadastrar_spinnerEstados)).setSelection(18);
        ((Spinner) mView.findViewById(R.id.cadastrar_spinnerTipoServico)).setSelection(0);
    }

    private void setCampos(int id, String data) {
        if (id == R.id.cadastrar_edtComplementoId) {
            ((EditText) mView.findViewById(id)).setText(data);
        } else {
            ((TextInputLayout) mView.findViewById(id)).getEditText().setText(data);
        }
    }

    private void setDadosCliente(Cliente cliente) {
        setCampos(R.id.cadastrar_edtNomeClienteId, cliente.getNome());
    }

    void setDadosEndereco(Endereco endereco) {
        setCampos(R.id.cadastrar_edtCepId, endereco.getCep());
        setCampos(R.id.cadastrar_edtRuaId, endereco.getLogradouro());
        setCampos(R.id.cadastrar_edtNumeroId, endereco.getNumero());
        setCampos(R.id.cadastrar_edtBairroId, endereco.getBairro());
        setCampos(R.id.cadastrar_edtCidadeId, endereco.getLocalidade());
        setCampos(R.id.cadastrar_edtComplementoId, endereco.getComplemento());
        setSpinner(R.id.cadastrar_spinnerEstados, R.array.estados, endereco.getUf());
    }

    private void setSpinner(int id, int arrayId, String data) {
        String[] arraySpinner = mView.getResources().getStringArray(arrayId);

        for (int i = 0; i < arraySpinner.length; i++) {
            if (data.equals(arraySpinner[i])) {
                ((Spinner) mView.findViewById(id)).setSelection(i);
                return;
            }
        }
        ((Spinner) mView.findViewById(id)).setSelection(0);
    }

    public void setDadosOrdemServico(OrdemServico os) {
        setDadosCliente(os.getCliente());
        setDadosEndereco(os.getEndereco());
        setSpinner(R.id.cadastrar_spinnerTipoServico, R.array.lista_servico, os.getTipoServico());
        ((TextView) mView.findViewById(R.id.editar_tvNumOs)).append(" " + os.getOrdemServicoId());
    }

    public Cliente createCliente(String nomeCliente) {
        return new Cliente(nomeCliente, nomeCliente.substring(0, 3));
    }

    public void menuItemAjuda() {
        String siteAjuda = "http://www.sinapseinformatica.com.br/";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(siteAjuda));
        if (fragment != null) {
            fragment.startActivity(intent);
        } else {
            activity.startActivity(intent);
        }
    }

    public void alertDialog(String titulo, String mensagem, boolean cancelable) {
        new AlertDialog.Builder(Objects.requireNonNull(fragment.getActivity()))
                .setTitle(titulo)
                .setMessage(mensagem)
                .setCancelable(cancelable)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (fragment.getFragmentManager() != null) {
                            if ((fragment.getFragmentManager().findFragmentByTag("CADASTRAR")) != null &&
                                    (fragment.getFragmentManager().findFragmentByTag("CADASTRAR").isVisible())) {
                                fragment.getFragmentManager().popBackStack();
                            } else if ((fragment.getFragmentManager().findFragmentByTag("EDITAR")) != null &&
                                    (fragment.getFragmentManager().findFragmentByTag("EDITAR").isVisible())) {
                                fragment.getFragmentManager().popBackStack();
                            }
                        }
                    }
                })
                .create()
                .show();
    }

    public boolean checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) fragment.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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
                if (((TextInputLayout) mView.findViewById(id)).getEditText().getText().toString().trim().isEmpty()) {
                    ((TextInputLayout) mView.findViewById(id)).setError("Digite o nome do cliente");
                    return false;
                } else {
                    ((TextInputLayout) mView.findViewById(id)).setError(null);
                    ((TextInputLayout) mView.findViewById(id)).setErrorEnabled(false);
                    return true;
                }
            case R.id.cadastrar_edtRuaId:
                if (((TextInputLayout) mView.findViewById(id)).getEditText().getText().toString().trim().isEmpty()) {
                    ((TextInputLayout) mView.findViewById(id)).setError("Digite o nome da rua");
                    return false;
                } else {
                    ((TextInputLayout) mView.findViewById(id)).setError(null);
                    ((TextInputLayout) mView.findViewById(id)).setErrorEnabled(false);
                    return true;
                }
            case R.id.cadastrar_edtBairroId:
                if (((TextInputLayout) mView.findViewById(id)).getEditText().getText().toString().trim().isEmpty()) {
                    ((TextInputLayout) mView.findViewById(id)).setError("Informe o bairro");
                    return false;
                } else {
                    ((TextInputLayout) mView.findViewById(id)).setError(null);
                    ((TextInputLayout) mView.findViewById(id)).setErrorEnabled(false);
                    return true;
                }
            case R.id.cadastrar_edtCepId:
                if (((TextInputLayout) mView.findViewById(id)).getEditText().getText().toString().trim().isEmpty()) {
                    ((TextInputLayout) mView.findViewById(id)).setError("Digite o cep");
                    return false;
                } else {
                    ((TextInputLayout) mView.findViewById(id)).setError(null);
                    ((TextInputLayout) mView.findViewById(id)).setErrorEnabled(false);
                    return true;
                }
            case R.id.cadastrar_edtNumeroId:
                if (((TextInputLayout) mView.findViewById(id)).getEditText().getText().toString().trim().isEmpty()) {
                    ((TextInputLayout) mView.findViewById(id)).setError("Digite o número");
                    return false;
                } else {
                    ((TextInputLayout) mView.findViewById(id)).setError(null);
                    ((TextInputLayout) mView.findViewById(id)).setErrorEnabled(false);
                    return true;
                }
            case R.id.cadastrar_edtCidadeId:
                if (((TextInputLayout) mView.findViewById(id)).getEditText().getText().toString().trim().isEmpty()) {
                    ((TextInputLayout) mView.findViewById(id)).setError("Informe a cidade");
                    return false;
                } else {
                    ((TextInputLayout) mView.findViewById(id)).setError(null);
                    ((TextInputLayout) mView.findViewById(id)).setErrorEnabled(false);
                    return true;
                }
        }
        return true;
    }

    @SuppressLint("MissingPermission")
    public void getLocalizacaoGPS() {
        Log.v("GPS", "getLocalizacaoGPS");

        LocationManager locationManager = (LocationManager) fragment.getActivity().getSystemService(LOCATION_SERVICE);

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

        LocationManager locationManager = (LocationManager) fragment.getActivity().getSystemService(LOCATION_SERVICE);
        boolean ativado = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!ativado) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            fragment.startActivity(intent);
            return;
        }

        FusedLocationProviderClient flpc = LocationServices.getFusedLocationProviderClient(fragment.getActivity().getApplicationContext());

        flpc.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                geoReferenciamento(location);
            }
        });
    }

    private void geoReferenciamento(Location location) {
        Log.v("GPS", "geoReferenciamento");

        Geocoder geo = new Geocoder(fragment.getActivity().getApplicationContext(), Locale.getDefault());
        try {
            List<Address> enderecos = geo.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if ((enderecos != null) && (enderecos.size() > 0)) {
                Address address = enderecos.get(0);

                //Rua
                ((TextInputLayout) fragment.getActivity().findViewById(R.id.cadastrar_edtRuaId)).getEditText().setText(address.getThoroughfare());

                //Número
                if (address.getFeatureName().contains("-")) {
                    ((TextInputLayout) fragment.getActivity().findViewById(R.id.cadastrar_edtNumeroId)).setTag(address.getFeatureName().substring(0, address.getFeatureName().indexOf("-")));
                } else {
                    ((TextInputLayout) fragment.getActivity().findViewById(R.id.cadastrar_edtNumeroId)).getEditText().setText(address.getFeatureName());
                }

                //Bairro
                ((TextInputLayout) fragment.getActivity().findViewById(R.id.cadastrar_edtBairroId)).getEditText().setText(address.getSubLocality());

                //Cidade
                ((TextInputLayout) fragment.getActivity().findViewById(R.id.cadastrar_edtCidadeId)).getEditText().setText(address.getSubAdminArea());

                //Cep
                ((TextInputLayout) fragment.getActivity().findViewById(R.id.cadastrar_edtCepId)).getEditText().setText(address.getPostalCode());

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
            Toast.makeText(fragment.getActivity(), "Aguardando triangulação do gps", Toast.LENGTH_SHORT).show();
        }
    }
}
