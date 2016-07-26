package com.estsoft.daummapsapplication;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import uk.co.senab.photoview.PhotoViewAttacher;

public class PhotoviewActivity extends AppCompatActivity {
    ImageView imageview;
    PhotoViewAttacher attacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photoview);

        imageview = (ImageView)findViewById(R.id.icon);

        // Set the Drawable displayed
        Drawable bitmap = getResources().getDrawable(R.drawable.jeong2);
        imageview.setImageDrawable(bitmap);

        attacher = new PhotoViewAttacher(imageview);

    }
}
