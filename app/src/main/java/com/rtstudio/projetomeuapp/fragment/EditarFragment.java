package com.rtstudio.projetomeuapp.fragment;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.rtstudio.projetomeuapp.R;
import com.rtstudio.projetomeuapp.classes.CepListener;
import com.rtstudio.projetomeuapp.classes.TextListener;
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

    private Cliente mCliente = null;
    private Endereco mEndereco = null;
    private OrdemServico mOrdemServico = null;
    private Utilitaria mUtil;
    private Repositorio mRepositorio;
    private Button mBotaoSalvar;
    private Spinner mEstados;
    private Spinner mTipoServico;
    private TextInputLayout mNomeCliente;
    private TextInputLayout mRua;
    private TextInputLayout mCep;
    private TextInputLayout mBairro;
    private TextInputLayout mNumero;
    private TextInputLayout mCidade;
    private TextInputLayout mProduto;
    private EditText mComplemento;
    private MyViewModel mMyViewModel;
    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_editar, container, false);

        inicializarVariaveis(mView);

        getActivity().setTitle(getString(R.string.editar_ordem_servico));

        mRepositorio = new Repositorio(getContext());

        mBotaoSalvar = mView.findViewById(R.id.fragment_cadastrar_btnCriarOSId);
        mBotaoSalvar.setText(getString(R.string.salvar));
        mBotaoSalvar.setEnabled(false);
        mBotaoSalvar.setAlpha(.5f);

        mUtil = new Utilitaria(this, mView);

        mCliente = new Cliente();
        mEndereco = new Endereco();
        mOrdemServico = new OrdemServico();

        OrdemServico os = null;
        if (getArguments() != null) {
            os = new Gson().fromJson(getArguments().getString("ORDEM_SERVICO"), OrdemServico.class);
            mCliente.setClienteId(os.getCliente().getClienteId());
            mEndereco.setEnderecoId(os.getEndereco().getEnderecoId());
            mOrdemServico.setOrdemServicoId(os.getOrdemServicoId());
            mOrdemServico.setFilename(os.getFilename());
            mOrdemServico.setSyncStatus(os.getSyncStatus());

            mUtil.setDadosOrdemServico(os);
        }

        mCep.getEditText().addTextChangedListener(new CepListener(this, os.getEndereco().getCep()));

        mNomeCliente.getEditText().addTextChangedListener(new TextListener(this, mNomeCliente.getId(), os.getCliente().getNome()));
        mRua.getEditText().addTextChangedListener(new TextListener(this, mRua.getId(), os.getEndereco().getLogradouro()));
        mComplemento.addTextChangedListener(new TextListener(this, mComplemento.getId(), os.getEndereco().getComplemento()));
        mBairro.getEditText().addTextChangedListener(new TextListener(this, mBairro.getId(), os.getEndereco().getBairro()));
        mNumero.getEditText().addTextChangedListener(new TextListener(this, mNumero.getId(), os.getEndereco().getNumero()));
        mCidade.getEditText().addTextChangedListener(new TextListener(this, mCidade.getId(), os.getEndereco().getLocalidade()));

        mView.findViewById(R.id.fragment_cadastrar_btnCriarOSId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validarInputDoUsuario()) {
                    return;
                }

                mCliente.setNome(mNomeCliente.getEditText().getText().toString());
                mCliente.setCodigoCliente(mNomeCliente.getEditText().getText().toString().substring(0, 3));

                mEndereco.setCep(mCep.getEditText().getText().toString());
                mEndereco.setLogradouro(mRua.getEditText().getText().toString());
                mEndereco.setNumero(mNumero.getEditText().getText().toString());
                mEndereco.setLocalidade(mCidade.getEditText().getText().toString());
                mEndereco.setUf(mEstados.getSelectedItem().toString());
                mEndereco.setBairro(mBairro.getEditText().getText().toString());
                mEndereco.setComplemento(mComplemento.getText().toString());

                mOrdemServico.setCliente(mCliente);
                mOrdemServico.setEndereco(mEndereco);
                mOrdemServico.setTipoServico(mTipoServico.getSelectedItem().toString());
                mProduto = getView().findViewById(R.id.fragment_editar_produto_TextInputLayout);
                mOrdemServico.setProduto(mProduto.getEditText().getText().toString());

                if (mRepositorio.atualizar(mOrdemServico)) {
                    getActivity().setResult(RESULT_OK, new Intent().putExtra("ORDEM_SERVICO_EDITADA", new Gson().toJson(mOrdemServico)));
                    mUtil.alertDialog("Aviso", "O.S. Editada com sucesso", false);
                } else {
                    mUtil.alertDialog("Aviso", "Não foi possível editar O.S.", false);
                }
            }
        });

        mView.findViewById(R.id.cadastrar_btnLocation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUtil.getLocalizacao();
            }
        });

        EditarProdutoFragment editarProdutoFragment = new EditarProdutoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("PRODUTO", os.getProduto());
        editarProdutoFragment.setArguments(bundle);

        if (getFragmentManager() != null) {
            getFragmentManager()
                    .beginTransaction()
                    .disallowAddToBackStack()
                    .replace(R.id.cadastrar_container_fragment, editarProdutoFragment)
                    .commit();
        }

        OrdemServico finalOs = os;
        mEstados.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!parent.getItemAtPosition(position).toString().equals(finalOs.getEndereco().getUf())) {
                    mUtil.desbloquearBotaoSalvar(true);
                } else {
                    mUtil.desbloquearBotaoSalvar(false);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        OrdemServico finalOs1 = os;
        mTipoServico.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!parent.getItemAtPosition(position).toString().equals(finalOs1.getTipoServico())) {
                    mUtil.desbloquearBotaoSalvar(true);
                } else {
                    mUtil.desbloquearBotaoSalvar(false);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        return mView;
    }

    private void inicializarVariaveis(View view) {
        mNomeCliente = view.findViewById(R.id.cadastrar_edtNomeClienteId);
        mRua = view.findViewById(R.id.cadastrar_edtRuaId);
        mComplemento = view.findViewById(R.id.cadastrar_edtComplementoId);
        mBairro = view.findViewById(R.id.cadastrar_edtBairroId);
        mCep = view.findViewById(R.id.cadastrar_edtCepId);
        mNumero = view.findViewById(R.id.cadastrar_edtNumeroId);
        mCidade = view.findViewById(R.id.cadastrar_edtCidadeId);
        mEstados = view.findViewById(R.id.cadastrar_spinnerEstados);
        mTipoServico = view.findViewById(R.id.cadastrar_spinnerTipoServico);
    }

    @Override
    public void onPause() {
        super.onPause();
//        mMyViewModel = ViewModelProviders.of(getActivity()).get(MyViewModel.class);
//        if (mOrdemServico != null) {
//            mMyViewModel.atualizarOS(mOrdemServico);
//        }
    }

    private boolean validarInputDoUsuario() {
        return mUtil.validarCampos(R.id.cadastrar_edtNomeClienteId, R.id.cadastrar_edtRuaId, R.id.cadastrar_edtBairroId,
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
            mUtil.limparCampos(
                    R.id.cadastrar_edtNomeClienteId,
                    R.id.cadastrar_edtRuaId,
                    R.id.cadastrar_edtComplementoId,
                    R.id.cadastrar_edtBairroId,
                    R.id.cadastrar_edtCepId,
                    R.id.cadastrar_edtNumeroId,
                    R.id.cadastrar_edtCidadeId
            );
        } else if (id == R.id.menu_itemAjuda) {
            mUtil.menuItemAjuda();
        }
        return super.onOptionsItemSelected(item);
    }
}
