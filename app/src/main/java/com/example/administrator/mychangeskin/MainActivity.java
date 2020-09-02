package com.example.administrator.mychangeskin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.administrator.lib.SkinManger;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button cancel = findViewById(R.id.cancel);
        Button change = findViewById(R.id.change);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SkinManger.getInstance().loadSkin(null);
            }
        });
//        红色 -3261126
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SkinManger.getInstance().loadSkin("/data/data/com.example.administrator.mychangeskin/skin/debug/skin-debug.apk");
            }
        });
//        黑色  -13158091
    }


}
