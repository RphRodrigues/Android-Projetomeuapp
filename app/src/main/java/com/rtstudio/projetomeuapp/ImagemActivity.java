package com.rtstudio.projetomeuapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.github.chrisbanes.photoview.PhotoView;
import com.rtstudio.projetomeuapp.preferencias.PreferenciasUsuario;

public class ImagemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferenciasUsuario.Companion.setTema(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagem);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(new Utilitaria(this).getWhiteArrow());

        if (PreferenciasUsuario.Companion.getPreferenciaTema(this).equals(PreferenciasUsuario.TEMA_NOTURNO)) {
            (findViewById(R.id.layout_activity_imagem)).setBackgroundColor(Color.BLACK);
        } else {
            (findViewById(R.id.layout_activity_imagem)).setBackgroundColor(Color.WHITE);
        }

        Bitmap bitmap;
        PhotoView mPhotoView = findViewById(R.id.imagem_photoView);

        if (getIntent().hasExtra("IMG")) {
            String imageName = getIntent().getStringExtra("IMG");
            int id = getIntent().getIntExtra("ID", 0);

            bitmap = BitmapFactory.decodeFile(imageName);
            mPhotoView.setImageBitmap(bitmap);

            getSupportActionBar().setTitle("Número O.S. " + id);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }/* else if (id == R.id.menu_imagem_compartilhar) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            //TODO continuar a intent para enviar imagem
        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_imagem, menu);
        return super.onCreateOptionsMenu(menu);
    }
}