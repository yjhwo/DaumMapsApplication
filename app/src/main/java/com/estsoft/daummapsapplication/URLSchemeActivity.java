package com.estsoft.daummapsapplication;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class URLSchemeActivity extends AppCompatActivity {
    private String url = "daummaps://route?sp=35.8241706,127.1480532&ep=35.8551190,127.1443080&by=FOOT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urlscheme);

        Intent intent = onRoute();

        if(intent!=null)
            startActivity(intent);


        Log.e("URLSchemeActivity","URLSchemeActivity");
    }

    private Intent onRoute(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));

        CustomSchemeURL daummap = new CustomSchemeURL(this, intent) {
            @Override
            public boolean canOpenDaummapURL() {
                return super.canOpenDaummapURL();
            }
        };

        if(daummap.existDaummapApp()){
            return intent;
        } else {
            CustomSchemeURL.openDaummapDownloadPage(this);
        }
        return null;
    }


}
