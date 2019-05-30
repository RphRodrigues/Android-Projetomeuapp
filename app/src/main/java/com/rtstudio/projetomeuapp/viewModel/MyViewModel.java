package com.rtstudio.projetomeuapp.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.rtstudio.projetomeuapp.modelo.OrdemServico;

public class MyViewModel extends AndroidViewModel {

    private OrdemServico mOrdemServico;

    public MyViewModel(@NonNull Application application) {
        super(application);
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
