package com.wsh.mypracticeapplication;

import android.graphics.drawable.Drawable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LoaderActivity extends AppCompatActivity {

    Loader<Drawable> loader;
    LoaderManager LM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);

    }
}
