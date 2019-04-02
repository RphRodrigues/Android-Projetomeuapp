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
import com.rtstudio.projetomeuapp.classes.modeloOS;

import java.util.List;

public class OrdemServicoAdapter extends RecyclerView.Adapter<OrdemServicoAdapter.MyViewHolder> {

    private Context mContext;
    private List<modeloOS> ordemServicoList;

    public OrdemServicoAdapter(Context context, List<modeloOS> list) {
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

        modeloOS ordemServico = ordemServicoList.get(position);

        holder.numOS.setText(String.valueOf(ordemServico.getNumOS()));

        holder.tipoServico.setText(ordemServico.getTipoServico());

        holder.nomeCliente.setText(ordemServico.getNomeCliente());
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