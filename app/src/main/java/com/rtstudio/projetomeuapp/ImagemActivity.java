package com.rtstudio.projetomeuapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.chrisbanes.photoview.PhotoView;

public class ImagemActivity extends AppCompatActivity {

    private PhotoView mPhotoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagem);

        Bitmap bitmap;
        mPhotoView = findViewById(R.id.imagem_photoView);

        if (getIntent().hasExtra("IMG")) {
            String imageName = getIntent().getStringExtra("IMG");
            bitmap = BitmapFactory.decodeFile(imageName);
            mPhotoView.setImageBitmap(bitmap);
        }
    }
}
