package com.example.cosmo.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class Audio extends AppCompatActivity {
    WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        web = (WebView) findViewById(R.id.web);


        String url = getIntent().getStringExtra("campoBusq");
        web.setWebViewClient(new WebViewClient());

        //web.setInitialScale(100);
        web.loadUrl("https://www.google.com/search?safe=off&source=lnms&tbm=vid&q=asa"+url);
    }
    public void irGoogle(View view){
        web.loadUrl("https://www.google.es/search?q="+getIntent().getStringExtra("campoBusq"));
    }

    public void nueva(View view){
        Toast.makeText(this, "Nueva busqueda", Toast.LENGTH_SHORT).show();
        Intent nueva = new Intent(this, MainActivity.class);
        nueva.putExtra("mailBusqueda", getIntent().getStringExtra("email"));
        startActivity(nueva);
    }
}
