package com.rtstudio.projetomeuapp.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.rtstudio.projetomeuapp.modelo.OrdemServico;

public class MyViewModel extends AndroidViewModel {

    private final MutableLiveData<String> mProduto = new MutableLiveData<>();
    private OrdemServico mOrdemServico;

    public MyViewModel(@NonNull Application application) {
        super(application);
    }

    public void setProduto(String produto) {
        mProduto.setValue(produto);
    }

    public LiveData<String> getProduto() {
        return mProduto;
    }

    public OrdemServico getmOrdemServico() {
        if (mOrdemServico == null) {
            return null;
        }
        return mOrdemServico;
    }

    public void criarOS(OrdemServico ordemServico) {
        mOrdemServico = ordemServico;
    }

    public void atualizarOS(OrdemServico ordemServico) {
        mOrdemServico = ordemServico;
    }
}
