package com.example.javaassist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.title).setOnClickListener(new View.OnClickListener() {
            @Override
            @ToastAnnotation("ddddd")
            public void onClick(View v) {

                Log.i("nole","nole ------- ");
            }
        });
    }
}