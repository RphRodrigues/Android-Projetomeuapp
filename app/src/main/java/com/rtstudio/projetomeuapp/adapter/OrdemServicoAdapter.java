package com.rtstudio.projetomeuapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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

        final OrdemServico ordemServico = ordemServicoList.get(position);

        holder.numOS.setText(String.valueOf(ordemServico.getOrdemServicoId()));

        holder.tipoServico.setText(ordemServico.getTipo());

        holder.bairro.setText(ordemServico.getEndereco().getBairro());

        holder.imageMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri mapUri = Uri.parse("geo:0,0?q=" + ordemServico.getEndereco().getNumero().trim() + " " +
                        ordemServico.getEndereco().getRua().trim() + "," + " " +
                        ordemServico.getEndereco().getBairro().trim() + "," + " " +
                        ordemServico.getEndereco().getCidade().trim() + "," + " " +
                        ordemServico.getEndereco().getEstado()
                );

                Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
                mContext.startActivity(mapIntent);
            }
        });

        holder.imageCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Camera", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return ordemServicoList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView numOS;
        TextView tipoServico;
        TextView bairro;
        ImageButton imageMap;
        ImageButton imageCam;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            numOS = itemView.findViewById(R.id.card_tvNumOs);
            tipoServico = itemView.findViewById(R.id.card_tvTipoServico);
            bairro = itemView.findViewById(R.id.card_tvBairro);
            imageMap = itemView.findViewById(R.id.card_ibMap);
            imageCam = itemView.findViewById(R.id.card_ibCamera);
        }
    }
}