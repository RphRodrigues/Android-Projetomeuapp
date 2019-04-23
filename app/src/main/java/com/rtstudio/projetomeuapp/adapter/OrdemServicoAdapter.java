package com.rtstudio.projetomeuapp.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.rtstudio.projetomeuapp.CadastrarServicoActivity;
import com.rtstudio.projetomeuapp.DAO.OrdemServicoDAO;
import com.rtstudio.projetomeuapp.R;
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
                                Toast.makeText(activity, "Camera " + position, Toast.LENGTH_SHORT).show();
                                break;

                            case R.id.floating_context_menu_galeria:
                                Toast.makeText(activity, "Galeria " + position, Toast.LENGTH_SHORT).show();
                                break;

                            default:
                                break;
                        }
                        return false;
                    }
                });

                popup.show();

//                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, 1);
//                }
//
//                fileFoto = new File(activity.getCacheDir(), "minhaFoto" + position + ".jpg");
//
//                Uri uri = FileProvider.getUriForFile(activity, "com.rtstudio.projetomeuapp.fileprovider", fileFoto);
//
//                Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//                camIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);   //usando para foto original
//
//                activity.startActivityForResult(camIntent, 3);
//
////                    if (camIntent.resolveActivity(activity.getPackageManager()) != null) {
////                        File fotoF = null;
////                        try {
////                            fotoF = createImageFile();
////                        } catch (IOException e) {
////                            e.printStackTrace();
////                        }
////                        if (fotoF != null) {
////                        Uri fotoURI = FileProvider.getUriForFile(activity.getBaseContext(), "com.rtstudio.projetomeuapp", fotoF);
////
////                        camIntent.putExtra(MediaStore.EXTRA_OUTPUT, fotoURI);
////                            activity.startActivityForResult(camIntent, 3);
////                        }
////
////                    }
//
//                Toast.makeText(activity, "Camera", Toast.LENGTH_SHORT).show();
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


    public File getFileFoto() {
        return fileFoto;
    }

    private File createImageFile() throws IOException {
        String currentPhotoPath;
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,   /* prefix */
                ".jpg",   /* suffix */
                storageDir       /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
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