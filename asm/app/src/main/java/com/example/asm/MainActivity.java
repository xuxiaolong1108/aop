package com.example.asm;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.title).setOnClickListener(new View.OnClickListener() {
            @Override
            @ConfirmAnnotation("卧槽 你要提交吗??????")
            public void onClick(View v) {

                Toast.makeText(v.getContext(),"我确定要提交",Toast.LENGTH_SHORT).show();
            }


        });


    }

    @ConfirmAnnotation("卧槽 你要提交吗??????")
    public void onClick(View view) {
        Log.i("nole","nole----------------1");
    }

}