package com.rtstudio.projetomeuapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rtstudio.projetomeuapp.R;
import com.rtstudio.projetomeuapp.classes.OrdemServico;

import java.util.List;

public class OrdemServicoAdapter extends RecyclerView.Adapter<OrdemServicoAdapter.MyViewHolder> {

    private Context mContext;
    private List<OrdemServico> ordemServicoList;

    public OrdemServicoAdapter(Context context, List<OrdemServico> list) {
        this.ordemServicoList = list;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        Log.v("LOG", "onCreateViewHolder");

        LayoutInflater LayoutInflater = android.view.LayoutInflater.from(mContext);

        View view = LayoutInflater.inflate(R.layout.os_card_item, null);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Log.v("LOG", "onBindViewHolder");

        OrdemServico ordemServico = ordemServicoList.get(position);

        holder.numOS.setText(String.valueOf(ordemServico.getOrdemServicoId()));

        holder.tipoServico.setText(ordemServico.getTipo());

        holder.nomeCliente.setText(ordemServico.getCliente().getNome());
    }

    @Override
    public int getItemCount() {
        return ordemServicoList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView numOS;
        TextView tipoServico;
        TextView nomeCliente;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            numOS = itemView.findViewById(R.id.card_tvNumOs);
            tipoServico = itemView.findViewById(R.id.card_tvTipoServico);
            nomeCliente = itemView.findViewById(R.id.card_tvNomeCliente);
        }
    }
}