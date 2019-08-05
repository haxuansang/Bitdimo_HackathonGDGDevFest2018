package com.appproteam.sangha.bitdimo;


import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.bumptech.glide.Glide;

public class ZoomImageActivity extends AppCompatActivity {
    String urlImage;
    ImageView zoomImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_image);
        zoomImage = (ImageView)findViewById(R.id.imv_zoom);
        urlImage=getIntent().getStringExtra("urlImage");

            Glide.with(this).load(urlImage).fitCenter().into(zoomImage);
        zoomImage.setOnTouchListener(new ImageMatrixTouchHandler(getApplicationContext()));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
