package com.rtstudio.projetomeuapp.fragment;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.rtstudio.projetomeuapp.R;
import com.rtstudio.projetomeuapp.classes.CepListener;
import com.rtstudio.projetomeuapp.modelo.Cliente;
import com.rtstudio.projetomeuapp.modelo.Endereco;
import com.rtstudio.projetomeuapp.modelo.OrdemServico;
import com.rtstudio.projetomeuapp.util.Utilitaria;
import com.rtstudio.projetomeuapp.repositorio.Repositorio;
import com.rtstudio.projetomeuapp.viewModel.MyViewModel;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditarFragment extends Fragment {

    private Cliente cliente = null;
    private Endereco endereco = null;
    private OrdemServico ordemServico = null;
    private Utilitaria util;
    private Repositorio mRepositorio;
    private MyViewModel mMyViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_editar, container, false);

        getActivity().setTitle(getString(R.string.editar_ordem_servico));

        mRepositorio = new Repositorio(getContext());

        ((Button) view.findViewById(R.id.cadastrar_btnCriarOSId)).setText("Salvar");

        util = new Utilitaria(this, view);

        cliente = new Cliente();
        endereco = new Endereco();
        ordemServico = new OrdemServico();

        final OrdemServico os;
        if (getArguments() != null) {
            os = new Gson().fromJson(getArguments().getString("ORDEM_SERVICO"), OrdemServico.class);
            cliente.setClienteId(os.getCliente().getClienteId());
            endereco.setEnderecoId(os.getEndereco().getEnderecoId());
            ordemServico.setOrdemServicoId(os.getOrdemServicoId());
            ordemServico.setFilename(os.getFilename());
            ordemServico.setSyncStatus(os.getSyncStatus());

            util.setDadosOrdemServico(os);

            Bitmap img = BitmapFactory.decodeFile(os.getFilename());
//            ((ImageView) findViewById(R.id.cadastrar_ivBitmap)).setImageBitmap(img);
        }

        ((TextInputLayout) view.findViewById(R.id.cadastrar_edtCepId)).getEditText().addTextChangedListener(new CepListener(getContext()));

        view.findViewById(R.id.cadastrar_btnCriarOSId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validarInputDoUsuario()) {
                    return;
                }

                String nomeCliente = ((TextInputLayout) view.findViewById(R.id.cadastrar_edtNomeClienteId)).getEditText().getText().toString();
                String rua = ((TextInputLayout) view.findViewById(R.id.cadastrar_edtRuaId)).getEditText().getText().toString();
                String complemento = ((EditText) view.findViewById(R.id.cadastrar_edtComplementoId)).getText().toString();
                String bairro = ((TextInputLayout) view.findViewById(R.id.cadastrar_edtBairroId)).getEditText().getText().toString();
                String cep = ((TextInputLayout) view.findViewById(R.id.cadastrar_edtCepId)).getEditText().getText().toString();
                String numero = ((TextInputLayout) view.findViewById(R.id.cadastrar_edtNumeroId)).getEditText().getText().toString();
                String cidade = ((TextInputLayout) view.findViewById(R.id.cadastrar_edtCidadeId)).getEditText().getText().toString();
                String estado = ((Spinner) view.findViewById(R.id.cadastrar_spinnerEstados)).getSelectedItem().toString();
                String tipoServico = ((Spinner) view.findViewById(R.id.cadastrar_spinnerTipoServico)).getSelectedItem().toString();

                cliente.setNome(nomeCliente);
                cliente.setCodigoCliente(nomeCliente.substring(0, 3));

                endereco.setCep(cep);
                endereco.setLogradouro(rua);
                endereco.setNumero(numero);
                endereco.setLocalidade(cidade);
                endereco.setUf(estado);
                endereco.setBairro(bairro);
                endereco.setComplemento(complemento);

                ordemServico.setCliente(cliente);
                ordemServico.setEndereco(endereco);
                ordemServico.setTipoServico(tipoServico);

                if (mRepositorio.atualizar(ordemServico)) {
                    getActivity().setResult(RESULT_OK, new Intent().putExtra("ORDEM_SERVICO_EDITADA", new Gson().toJson(ordemServico)));
                    util.alertDialog("Aviso", "O.S. Editada com sucesso", false);
                } else {
                    util.alertDialog("Aviso", "Não foi possível editar O.S.", false);
                }
            }
        });

        view.findViewById(R.id.cadastrar_btnLocation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                util.getLocalizacao();
            }
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        mMyViewModel = ViewModelProviders.of(getActivity()).get(MyViewModel.class);
        if (ordemServico != null) {
            mMyViewModel.atualizarOS(ordemServico);
        }
    }

    private boolean validarInputDoUsuario() {
        return util.validarCampos(R.id.cadastrar_edtNomeClienteId, R.id.cadastrar_edtRuaId, R.id.cadastrar_edtBairroId,
                R.id.cadastrar_edtCepId, R.id.cadastrar_edtCidadeId, R.id.cadastrar_edtNumeroId, R.id.cadastrar_edtComplementoId);
    }

    public String getUriCep(View view) {
        return "https://viacep.com.br/ws/" + ((TextInputLayout) view.findViewById(R.id.cadastrar_edtCepId)).getEditText().getText() + "/json/";
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            getActivity().finish();
        } else if (id == R.id.menu_itemLimpar) {
            util.limparCampos(
                    R.id.cadastrar_edtNomeClienteId,
                    R.id.cadastrar_edtRuaId,
                    R.id.cadastrar_edtComplementoId,
                    R.id.cadastrar_edtBairroId,
                    R.id.cadastrar_edtCepId,
                    R.id.cadastrar_edtNumeroId,
                    R.id.cadastrar_edtCidadeId
            );
        } else if (id == R.id.menu_itemAjuda) {
            util.menuItemAjuda();
        }
        return super.onOptionsItemSelected(item);
    }
}
