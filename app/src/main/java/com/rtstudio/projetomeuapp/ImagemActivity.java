package com.rtstudio.projetomeuapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class ImagemActivity extends AppCompatActivity {

    private ImageView mImagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagem);

        Bitmap bitmap;
        mImagem = findViewById(R.id.imagem_imageView);

        if (getIntent().hasExtra("IMG")) {
            String imageName = getIntent().getStringExtra("IMG");
            bitmap = BitmapFactory.decodeFile(imageName);
            mImagem.setImageBitmap(bitmap);

        }
    }
}
