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
import android.support.v4.app.Fragment;
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
import com.rtstudio.projetomeuapp.R;
import com.rtstudio.projetomeuapp.classes.OrdemServico;
import com.rtstudio.projetomeuapp.fragment.EditarFragment;
import com.rtstudio.projetomeuapp.notificacao.Notificacao;
import com.rtstudio.projetomeuapp.repositorio.Repositorio;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.support.v4.content.ContextCompat.checkSelfPermission;

public class OrdemServicoAdapter extends RecyclerView.Adapter<OrdemServicoAdapter.MyViewHolder> {

    public static final int REQUEST_CODE_CRIAR = 1;
    public static final int REQUEST_CODE_EDITAR = 2;
    public static final int REQUEST_CODE_CAMERA = 3;
    public static final int REQUEST_CODE_GALERIA = 4;

    private Activity activity;
    private Fragment fragment;
    private List<OrdemServico> ordemServicoList;
    private File fileFoto;
    private int posicaoAtualDoClick;

    public OrdemServicoAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    public OrdemServicoAdapter(Fragment fragment, List<OrdemServico> list) {
        this.ordemServicoList = list;
        this.fragment = fragment;
    }

    public OrdemServicoAdapter(Activity activity, List<OrdemServico> list) {
        this.ordemServicoList = list;
        this.activity = activity;
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

        LayoutInflater LayoutInflater = android.view.LayoutInflater.from(fragment.getContext().getApplicationContext());

        View view = LayoutInflater.inflate(R.layout.os_card_item, null);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        Log.v("LOG", "onBindViewHolder");

        final OrdemServico ordemServico = ordemServicoList.get(position);

        if (ordemServico.getSyncStatus() == OrdemServico.SYNC_STATUS_TRUE) {
            holder.imageStatus.setImageDrawable(fragment.getContext().getDrawable(R.drawable.ic_cloud_done_black_24dp));
        } else {
            holder.imageStatus.setImageDrawable(fragment.getContext().getDrawable(R.drawable.ic_sync_black_24dp));
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

                if (mapIntent.resolveActivity(fragment.getContext().getPackageManager()) != null) {
                    fragment.getContext().startActivity(mapIntent);
                }
            }
        });

        holder.imageCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(fragment.getContext(), holder.imageCam);

                popup.inflate(R.menu.popup_menu);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.floating_context_menu_camera:
                                if (checkSelfPermission(fragment.getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                                    fragment.requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);

                                    return false;
                                }
                                setPosicaoAtualDoClick(position);
                                tirarFoto();

                                break;

                            case R.id.floating_context_menu_galeria:
                                setPosicaoAtualDoClick(ordemServicoList.get(position).getOrdemServicoId());
                                if (checkSelfPermission(fragment.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                                    fragment.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_GALERIA);

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

                if (fragment.getFragmentManager() != null) {
                    EditarFragment editarFragment = new EditarFragment();

                    Bundle bundle = new Bundle();

                    bundle.putString("ORDEM_SERVICO", new Gson().toJson(ordemServico));

                    editarFragment.setArguments(bundle);

                    fragment.getFragmentManager().beginTransaction()
                            .addToBackStack("INICIO")
                            .replace(R.id.main_activity_fragment_area, editarFragment, "EDITAR")
                            .commit();
                }

//                Intent intentEditar = new Intent(fragment.getContext(), EditarOrdemServicoActivity.class);
//                intentEditar.putExtra("ORDEM_SERVICO", new Gson().toJson(ordemServico));
//                setPosicaoAtualDoClick(position);
//                fragment.getActivity().setResult(Activity.RESULT_OK);
//                fragment.getActivity().startActivityForResult(intentEditar, REQUEST_CODE_EDITAR);
            }
        });

        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                new AlertDialog.Builder(fragment.getContext())
                        .setTitle("Aviso")
                        .setMessage("Deseja realmente excluir a OS número " + ordemServicoList.get(position).getOrdemServicoId())
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int ordemServicoId = ordemServicoList.get(position).getOrdemServicoId();

                                new Notificacao().notificacaoBotao(fragment.getContext(), ordemServicoList.get(position));

                                new Repositorio(fragment.getContext()).deletar(ordemServicoId);

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
        fragment.getActivity().startActivityForResult(intentGaleria, REQUEST_CODE_GALERIA);
    }

    public File getFileFoto() {
        return fileFoto;
    }

    public void tirarFoto() {

        try {
            fileFoto = createImageFile(getPosicaoAtualDoClick());

            Uri uriFile = FileProvider.getUriForFile(fragment.getContext(), "com.rtstudio.projetomeuapp.fileprovider", fileFoto);

            Intent intentFoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            intentFoto.putExtra(MediaStore.EXTRA_OUTPUT, uriFile);

            fragment.getActivity().startActivityForResult(intentFoto, REQUEST_CODE_CAMERA);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private File createImageFile(int position) throws IOException {

        String dataHoraAtual = new SimpleDateFormat("yyyyMMdd_HHmmss", new Locale("pt-BR")).format(new Date());
        String imageName = "RPH_" + position + "-" + dataHoraAtual + "_";

        //File caminhaDaPasta = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File caminhaDaPasta = fragment.getContext().getCacheDir().getAbsoluteFile();

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