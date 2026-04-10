package com.mirea.shelmichas.looper;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

public class MyLooper extends Thread {

    public Handler handler;

    @Override
    public void run() {

        Log.d("LooperThread", "Поток запущен");

        Looper.prepare();

        handler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                Log.d("LooperThread", "Получено сообщение: " + msg.obj);
            }
        };

        Looper.loop();
    }
}