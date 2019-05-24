package com.rtstudio.projetomeuapp.adapter;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rtstudio.projetomeuapp.EditarOrdemServicoActivity;
import com.rtstudio.projetomeuapp.ImagemActivity;
import com.rtstudio.projetomeuapp.R;
import com.rtstudio.projetomeuapp.modelo.OrdemServico;
import com.rtstudio.projetomeuapp.notificacao.Notificacao;
import com.rtstudio.projetomeuapp.repositorio.Repositorio;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrdemServicoAdapter extends RecyclerView.Adapter<OrdemServicoAdapter.MyViewHolder> {

    public static final int REQUEST_CODE_CRIAR = 1;
    public static final int REQUEST_CODE_EDITAR = 2;
    public static final int REQUEST_CODE_CAMERA = 3;
    public static final int REQUEST_CODE_GALERIA = 4;

    private Activity mActivity;
    private List<OrdemServico> ordemServicoList;
    private File fileFoto;
    private int posicaoAtualDoClick;
    private boolean imagemAltetada = false;
    private AlertDialog mAlerta;

    public OrdemServicoAdapter(Activity activity, List<OrdemServico> list) {
        this.ordemServicoList = list;
        this.mActivity = activity;
    }

    public boolean isImagemAltetada() {
        return imagemAltetada;
    }

    public void setImagemAltetada(boolean imagemAltetada) {
        this.imagemAltetada = imagemAltetada;
    }

    public int getPosicaoAtualDoClick() {
        return posicaoAtualDoClick;
    }

    private void setPosicaoAtualDoClick(int posicaoAtualDoClick) {
        this.posicaoAtualDoClick = posicaoAtualDoClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        Log.v("LOG", "onCreateViewHolder");

        LayoutInflater LayoutInflater = android.view.LayoutInflater.from(mActivity.getApplicationContext());

        View view = LayoutInflater.inflate(R.layout.os_card_item, null);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        Log.v("LOG", "onBindViewHolder");

        final OrdemServico ordemServico = ordemServicoList.get(position);

        if (ordemServico.getSyncStatus() == OrdemServico.SYNC_STATUS_TRUE) {
            holder.imageStatus.setImageDrawable(mActivity.getDrawable(R.drawable.ic_cloud_done_black_24dp));
        } else {
            holder.imageStatus.setImageDrawable(mActivity.getDrawable(R.drawable.ic_sync_black_24dp));
        }

        holder.numOS.setText(String.valueOf(ordemServico.getOrdemServicoId()));

        holder.tipoServico.setText(ordemServico.getTipoServico());

        holder.bairro.setText(ordemServico.getEndereco().getBairro());

        holder.imageMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri mapUri = Uri.parse("geo:0,0?q=" + ordemServico.getEndereco().getNumero().trim() + " " +
                        ordemServico.getEndereco().getLogradouro().trim() + "," + " " +
                        ordemServico.getEndereco().getBairro().trim() + "," + " " +
                        ordemServico.getEndereco().getLocalidade().trim() + "," + " " +
                        ordemServico.getEndereco().getUf()
                );

                Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);

                if (mapIntent.resolveActivity(mActivity.getPackageManager()) != null) {
                    mActivity.startActivity(mapIntent);
                }
            }
        });

        holder.imageCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(mActivity, holder.imageCam);

                popup.inflate(R.menu.popup_menu);

                if (ordemServico.getFilename() == null || ordemServico.getFilename().isEmpty()) {
                    popup.getMenu().removeItem(R.id.popup_menu_imagem);
                }

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.popup_menu_camera:
                                if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                                    ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);

                                    return false;
                                }

                                setPosicaoAtualDoClick(position);

                                if (ordemServico.getFilename() != null) {
                                    setImagemAltetada(true);
                                }

                                tirarFoto();

                                break;

                            case R.id.popup_menu_galeria:
                                setPosicaoAtualDoClick(position);
                                if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                                    ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_GALERIA);

                                    return false;
                                }

                                if (ordemServico.getFilename() != null) {
                                    setImagemAltetada(true);
                                }

                                abrirGaleria();

                                break;

                            case R.id.popup_menu_imagem:
                                View view = mActivity.getLayoutInflater().inflate(R.layout.alerta_dialog_imagem, null);

                                Bitmap img = BitmapFactory.decodeFile(ordemServico.getFilename());
                                ((ImageView) view.findViewById(R.id.alerta_imagem)).setImageBitmap(img);

//                                view.findViewById(R.id.alerta_botao).setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        mAlerta.dismiss();
//                                    }
//                                });

                                view.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(mActivity, ImagemActivity.class);
                                        intent.putExtra("IMG", ordemServico.getFilename());
                                        intent.putExtra("ID", ordemServico.getOrdemServicoId());

                                        mActivity.startActivity(intent);
                                        mAlerta.dismiss();
                                    }
                                });
                                mAlerta = new AlertDialog.Builder(mActivity).setView(view).create();
                                mAlerta.show();

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
                Intent intentEditar = new Intent(mActivity, EditarOrdemServicoActivity.class);
                intentEditar.putExtra("ORDEM_SERVICO", new Gson().toJson(ordemServico));
                setPosicaoAtualDoClick(position);

                mActivity.setResult(Activity.RESULT_OK);
                mActivity.startActivityForResult(intentEditar, REQUEST_CODE_EDITAR);
            }
        });

        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                new AlertDialog.Builder(mActivity)
                        .setTitle("Aviso")
                        .setMessage("Deseja realmente excluir a OS número " + ordemServicoList.get(position).getOrdemServicoId())
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int ordemServicoId = ordemServicoList.get(position).getOrdemServicoId();

                                new Notificacao().notificacaoBotao(mActivity, ordemServicoList.get(position));

                                new Repositorio(mActivity).deletar(ordemServicoId);

                                ordemServicoList.remove(position);
                                notifyDataSetChanged();
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
        mActivity.startActivityForResult(intentGaleria, REQUEST_CODE_GALERIA);
    }

    public File getFileFoto() {
        return fileFoto;
    }

    public void tirarFoto() {

        try {
            fileFoto = createImageFile();

            Uri uriFile = FileProvider.getUriForFile(mActivity, "com.rtstudio.projetomeuapp.fileprovider", fileFoto);

            Intent intentFoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            intentFoto.putExtra(MediaStore.EXTRA_OUTPUT, uriFile);

            mActivity.startActivityForResult(intentFoto, REQUEST_CODE_CAMERA);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private File createImageFile() throws IOException {

        String dataHoraAtual = new SimpleDateFormat("yyyyMMdd_HHmmss", new Locale("pt-BR")).format(new Date());
        String imageName = "RPH_" + dataHoraAtual;

        //File caminhaDaPasta = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File caminhaDaPasta = mActivity.getCacheDir().getAbsoluteFile();

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
        private ImageView imageStatus;
        private View view;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            numOS = itemView.findViewById(R.id.card_tvNumOs);
            tipoServico = itemView.findViewById(R.id.card_tvTipoServico);
            bairro = itemView.findViewById(R.id.card_tvBairro);
            imageMap = itemView.findViewById(R.id.card_ibMap);
            imageCam = itemView.findViewById(R.id.card_ibCamera);
            imageStatus = itemView.findViewById(R.id.card_image_status);
            view = itemView;
        }
    }
}