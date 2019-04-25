package com.rtstudio.projetomeuapp.adapter;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.rtstudio.projetomeuapp.CadastrarServicoActivity;
import com.rtstudio.projetomeuapp.DAO.OrdemServicoDAO;
import com.rtstudio.projetomeuapp.R;
import com.rtstudio.projetomeuapp.TelaInicialActivity;
import com.rtstudio.projetomeuapp.classes.DAO.ArquivoDAO;
import com.rtstudio.projetomeuapp.classes.OrdemServico;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OrdemServicoAdapter extends RecyclerView.Adapter<OrdemServicoAdapter.MyViewHolder> {

    private Activity activity;
    private List<OrdemServico> ordemServicoList;
    private File fileFoto;
    private int posicaoGlobal;

    public OrdemServicoAdapter(Activity activity) {
        this.activity = activity;
    }

    public OrdemServicoAdapter(Activity activity, List<OrdemServico> list) {
        this.ordemServicoList = list;
        this.activity = activity;
    }

    public int getPosicaoGlobal() {
        return posicaoGlobal;
    }

    public void setPosicaoGlobal(int posicaoGlobal) {
        this.posicaoGlobal = posicaoGlobal;
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
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        Log.v("LOG", "onBindViewHolder");

        final OrdemServico ordemServico = ordemServicoList.get(position);

        holder.numOS.setText(String.valueOf(ordemServico.getOrdemServicoId()));

        holder.tipoServico.setText(ordemServico.getTipoServico());

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

                if (mapIntent.resolveActivity(activity.getPackageManager()) != null) {
                    activity.startActivity(mapIntent);
                }
            }
        });

        holder.imageCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(activity, holder.imageCam);

                popup.inflate(R.menu.floating_context_menu);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.floating_context_menu_camera:
                                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, TelaInicialActivity.PERMISSION_REQUEST_CAMERA);

                                    return false;
                                }
                                posicaoGlobal = position;
                                tirarFoto();

                                break;

                            case R.id.floating_context_menu_galeria:
                                posicaoGlobal = ordemServicoList.get(position).getOrdemServicoId();
                                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, TelaInicialActivity.PERMISSION_REQUEST_GALERIA);

                                    return false;
                                }
                                abrirGaleria();

                                break;

                            default:
                                break;
                        }
                        return true;
                    }
                });

                popup.show();
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
//                                new ArquivoDAO().salvarArquivo(ordemServicoList, new File(activity.getFilesDir(), "TCC.txt"));
                                new OrdemServicoDAO(activity).deleteOrdemServico(ordemServicoList.get(position).getOrdemServicoId());
                                ordemServicoList.remove(position);
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

    public void abrirGaleria() {
        Intent intentGaleria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(intentGaleria, TelaInicialActivity.PERMISSION_REQUEST_GALERIA);
    }

    public File getFileFoto() {
        return fileFoto;
    }

    public void tirarFoto() {

        try {
            fileFoto = createImageFile(posicaoGlobal);

            Uri uriFile = FileProvider.getUriForFile(activity, "com.rtstudio.projetomeuapp.fileprovider", fileFoto);

            Intent intentFoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            intentFoto.putExtra(MediaStore.EXTRA_OUTPUT, uriFile);

            activity.startActivityForResult(intentFoto, 3);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private File createImageFile(int position) throws IOException {

        @SuppressLint("SimpleDateFormat")
        String dataHoraAtual = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageName = "RPH_" + position + "-" + dataHoraAtual + "_";

        //File caminhaDaPasta = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File caminhaDaPasta = activity.getCacheDir().getAbsoluteFile();

        return File.createTempFile(imageName, ".jpg", caminhaDaPasta);
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