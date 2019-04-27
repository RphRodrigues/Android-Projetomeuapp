package com.rtstudio.projetomeuapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.rtstudio.projetomeuapp.DAO.OrdemServicoDAO;
import com.rtstudio.projetomeuapp.classes.Cliente;
import com.rtstudio.projetomeuapp.classes.Endereco;
import com.rtstudio.projetomeuapp.classes.OrdemServico;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CadastrarServicoActivity extends AppCompatActivity {

    public static final int PERMISSION_REQUEST_GPS = 100;
    public static final int PERMISSION_REQUEST_MEMORIA = 101;

    int position;
    File file;
    OrdemServicoDAO OrdemServicoDAO;
    private Cliente cliente = null;
    private Endereco endereco = null;
    private OrdemServico ordemServico = null;
    private EditText nomeCliente;
    private EditText rua;
    private EditText cep;
    private EditText complemento;
    private EditText bairro;
    private EditText numero;
    private EditText cidade;
    private EditText descricaoServico;
    private Button btnCriarOS;
    private Button btnLocalizar;
    private Spinner estado;
    private Spinner tipoServico;
    private List<OrdemServico> osList;
    private ImageView imgBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_servico);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_MEMORIA);

            return;
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inicilizarVariaveisDeClasse();

        if (getIntent().getExtras() != null) {
            editarOS();
            alertDialog("Não foi possível editar OS", true);
        }

        btnCriarOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (!validacao()) {
//                    return;
//                }

                createCliente();

                createEndereco();

                createOrdemServico();

                Bundle bundle = new Bundle();
                bundle.putParcelable("ORDEM_SERVICO", ordemServico);
                bundle.putInt("POSITION", position);

                Intent intent = new Intent();
                intent.putExtra("BUNDLE", bundle);
                setResult(RESULT_OK, intent);

                if (btnCriarOS.getText().toString().toLowerCase().equals("salvar")) {
                    alertDialog("Não foi possível editar OS", true);
                } else {
                    salvarOrdemServicoNoBancoDeDados();
                    alertDialog(getString(R.string.os_gerada_sucesso), false);
                }

            }
        });

        btnLocalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        || (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {

                    String[] permissoes = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

                    ActivityCompat.requestPermissions(CadastrarServicoActivity.this, permissoes, PERMISSION_REQUEST_GPS);
                    return;
                }
                getLocalizacao();
            }
        });
    }

    private void inicilizarVariaveisDeClasse() {
        nomeCliente = findViewById(R.id.cadastrar_edtNomeClienteId);
        rua = findViewById(R.id.cadastrar_edtRuaId);
        cep = findViewById(R.id.cadastrar_edtCepId);
        numero = findViewById(R.id.cadastrar_edtNumeroId);
        cidade = findViewById(R.id.cadastrar_edtCidadeId);
        estado = findViewById(R.id.cadastrar_spinnerEstados);
        complemento = findViewById(R.id.cadastrar_edtComplementoId);
        bairro = findViewById(R.id.cadastrar_edtBairroId);
        tipoServico = findViewById(R.id.cadastrar_spinnerId);
        descricaoServico = findViewById(R.id.cadastrar_edtDescricaoServicosId);
        btnCriarOS = findViewById(R.id.cadastrar_btnCriarOSId);
        btnLocalizar = findViewById(R.id.cadastrar_btnLocation);
        imgBitmap = findViewById(R.id.cadastrar_ivBitmap);

        //Inicializa o spinner de estados com RJ
        estado.setSelection(18);
    }

    private boolean salvarOrdemServicoNoBancoDeDados() {
        return new OrdemServicoDAO(CadastrarServicoActivity.this).insertOrdemServico(ordemServico);
    }

    private boolean atualizarOrdemServicoNoBancoDeDados() {
        return new OrdemServicoDAO(CadastrarServicoActivity.this).updateOS(ordemServico);
    }

    private void createOrdemServico() {
        ordemServico = new OrdemServico(
                cliente,
                endereco,
                descricaoServico.getText().toString(),
                tipoServico.getSelectedItem().toString()
        );
    }

    private void createEndereco() {
        endereco = new Endereco(
                cep.getText().toString(),
                rua.getText().toString(),
                numero.getText().toString(),
                cidade.getText().toString(),
                estado.getSelectedItem().toString(),
                bairro.getText().toString(),
                complemento.getText().toString()
        );
    }

    private void createCliente() {
        String codCliente = nomeCliente.getText().toString().substring(0, 3);

        cliente = new Cliente(
                nomeCliente.getText().toString(),
                codCliente
        );
    }

    @SuppressLint("MissingPermission")
    private void getLocalizacaoGPS() {
        Log.v("GPS", "getLocalizacaoGPS");

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

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
    private void getLocalizacao() {
        Log.v("GPS", "getLocalizacao");

        FusedLocationProviderClient flpc = LocationServices.getFusedLocationProviderClient(getApplicationContext());

        flpc.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                geoReferenciamento(location);
            }
        });
    }

    private void geoReferenciamento(Location location) {
        Log.v("GPS", "geoReferenciamento");

        Geocoder geo = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> enderecos = geo.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if ((enderecos != null) && (enderecos.size() > 0)) {
                Address address = enderecos.get(0);

                //Rua
                rua.setText(address.getThoroughfare());

                //Número
                if (address.getFeatureName().contains("-")) {
                    numero.setTag(address.getFeatureName().substring(0, address.getFeatureName().indexOf("-")));
                } else {
                    numero.setText(address.getFeatureName());
                }

                //Bairro
                bairro.setText(address.getSubLocality());

                //Cidade
                cidade.setText(address.getSubAdminArea());

                //Cep
                cep.setText(address.getPostalCode());

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
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_GPS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.v("PERMISSAO", "Permissão gps concedida");
                getLocalizacao();
            } else {
                Log.v("PERMISSAO", "Permissão gps negada");
                Toast.makeText(this, "O acesso a localização é necessário para utilizar o GPS.", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == PERMISSION_REQUEST_MEMORIA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.v("PERMISSAO", "Permissão memória concedida");

            } else {
                Log.v("PERMISSAO", "Permissão memória negada");
                Toast.makeText(this, "O acesso à memória é necessário para criar OS", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    private void editarOS() {
        Bundle b = getIntent().getBundleExtra("BUNDLE");
        OrdemServico os = b.getParcelable("ORDEM_SERVICO");
        position = b.getInt("POSITION");
        if (os != null) {
            nomeCliente.setText(os.getCliente().getNome());
            rua.setText(os.getEndereco().getRua());
            complemento.setText(os.getEndereco().getComplemento());
            bairro.setText(os.getEndereco().getBairro());
            cep.setText(os.getEndereco().getCep());
            numero.setText(os.getEndereco().getNumero());
            cidade.setText(os.getEndereco().getCidade());

//            String[] arrayEstados = {
//                    "AC", "AL", "AP", "AM", "BA",
//                    "CE", "DF", "ES", "GO", "MA",
//                    "MT", "MS", "MG", "PA", "PB",
//                    "PR", "PE", "PI", "RJ", "RN",
//                    "RS", "RO", "RR", "SC", "SP",
//                    "SE", "TO",
//            };
//
//            int i = 0;
//            for (String s : arrayEstados) {
//                if (s.equals(os.getEndereco().getEstado())) {
//                    estado.setSelection(i);
//                }
//                i++;
//            }
//
//            String[] arrayTipoServico = {"Instalação", "Reparo", "Desistalação"};
//
//            i = 0;
//            for (String s : arrayTipoServico) {
//                if (s.equals(os.getTipoServico())) {
//                    tipoServico.setSelection(i);
//                }
//                i++;
//            }
            descricaoServico.setText(os.getDescricaoServico());

            btnCriarOS.setText("Salvar");

            Bitmap img = BitmapFactory.decodeFile(os.getFilename());
            imgBitmap.setImageBitmap(img);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.menu_itemLimpar) {
            nomeCliente.setText("");
            rua.setText("");
            complemento.setText("");
            bairro.setText("");
            cep.setText("");
            numero.setText("");
            cidade.setText("");
            descricaoServico.setText("");
        } else if (id == R.id.menu_itemAjuda) {
            String siteAjuda = "http://www.sinapseinformatica.com.br/";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(siteAjuda));
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void alertDialog(String mensagem, boolean bool) {
        new AlertDialog.Builder(CadastrarServicoActivity.this)
                .setTitle("Aviso")
                .setMessage(mensagem)
                .setCancelable(!bool)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .create()
                .show();
    }

    private Boolean validacao() {
        //Validação para testar se o usuário inseriu os dados do cliente
        if (nomeCliente.getText().toString().isEmpty()) {
            Toast.makeText(CadastrarServicoActivity.this,
                    getString(R.string.insira_nome_cliente),
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        //Validação para testar se o usuário inseriu o endereço do serviço
        if (rua.getText().toString().isEmpty()) {
            Toast.makeText(CadastrarServicoActivity.this,
                    getString(R.string.preencha_endereco),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (bairro.getText().toString().isEmpty()) {
            Toast.makeText(CadastrarServicoActivity.this,
                    getString(R.string.preencha_endereco),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (cep.getText().toString().isEmpty()) {
            Toast.makeText(CadastrarServicoActivity.this,
                    getString(R.string.preencha_endereco),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (numero.getText().toString().isEmpty()) {
            Toast.makeText(CadastrarServicoActivity.this,
                    getString(R.string.preencha_endereco),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (cidade.getText().toString().isEmpty()) {
            Toast.makeText(CadastrarServicoActivity.this,
                    getString(R.string.preencha_endereco),
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        //Validação para testar se o usuário inseriu a descrição do serviço
        if (descricaoServico.getText().toString().isEmpty()) {
            Toast.makeText(CadastrarServicoActivity.this,
                    getString(R.string.preencha_descricao_servico),
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}