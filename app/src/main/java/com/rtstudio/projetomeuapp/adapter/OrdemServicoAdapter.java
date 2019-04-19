package com.rtstudio.projetomeuapp.adapter;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.rtstudio.projetomeuapp.CadastrarServicoActivity;
import com.rtstudio.projetomeuapp.R;
import com.rtstudio.projetomeuapp.classes.DAO.ArquivoDAO;
import com.rtstudio.projetomeuapp.classes.OrdemServico;

import java.io.File;
import java.util.List;

public class OrdemServicoAdapter extends RecyclerView.Adapter<OrdemServicoAdapter.MyViewHolder> {

    private Activity activity;
    private List<OrdemServico> ordemServicoList;

    public OrdemServicoAdapter(Activity activity, List<OrdemServico> list) {
        this.ordemServicoList = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        Log.v("LOG", "onCreateViewHolder");

        LayoutInflater LayoutInflater = android.view.LayoutInflater.from(activity.getApplicationContext());

        View view = LayoutInflater.inflate(R.layout.os_card_item, null);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

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
                activity.startActivity(mapIntent);
            }
        });

        holder.imageCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, 1);
                }
                Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                activity.startActivity(camIntent);

                Toast.makeText(activity, "Camera", Toast.LENGTH_SHORT).show();
            }
        });

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("ORDEM_SERVICO", ordemServico);
                bundle.putInt("POSITION", position);

                Intent intent = new Intent(activity, CadastrarServicoActivity.class);
                intent.putExtra("BUNDLE", bundle);

                activity.startActivityForResult(intent, 2);
            }
        });

        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                new AlertDialog.Builder(activity)
                        .setTitle("Aviso")
                        .setMessage("Deseja realmente excluir a OS número " + ordemServicoList.get(position).getOrdemServicoId())
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ordemServicoList.remove(position);
                                new ArquivoDAO().salvarArquivo(ordemServicoList, new File(activity.getFilesDir(), "TCC.txt"));
                                notifyItemRemoved(position);
                            }
                        })
                        .setNegativeButton("Cancelar", null)
                        .create()
                        .show();

                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return ordemServicoList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView numOS;
        private TextView tipoServico;
        private TextView bairro;
        private ImageButton imageMap;
        private ImageButton imageCam;
        private View view;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            numOS = itemView.findViewById(R.id.card_tvNumOs);
            tipoServico = itemView.findViewById(R.id.card_tvTipoServico);
            bairro = itemView.findViewById(R.id.card_tvBairro);
            imageMap = itemView.findViewById(R.id.card_ibMap);
            imageCam = itemView.findViewById(R.id.card_ibCamera);
            view = itemView;
        }
    }
}