package com.estsoft.daummapsapplication;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class URLSchemeActivity extends AppCompatActivity {
    private String url = "daummaps://open";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urlscheme);


        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);


        Log.e("URLSchemeActivity","URLSchemeActivity");
    }


}
