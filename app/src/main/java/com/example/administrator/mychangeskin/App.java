package com.example.administrator.mychangeskin;

import android.app.Application;

import com.example.administrator.lib.SkinManger;

/**
 * Created by Administrator on 2020/9/2.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SkinManger.init(this);
    }
}
