package com.example.aspectj;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.title).setOnClickListener(new View.OnClickListener() {
            @Override
            @ToastAnnotation("ddddd")
            public void onClick(View view) {
                Log.i("nole", "nole========");
            }
        });

    }
}