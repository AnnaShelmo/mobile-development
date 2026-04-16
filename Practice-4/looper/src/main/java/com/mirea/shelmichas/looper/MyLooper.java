package com.mirea.shelmichas.looper;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

public class MyLooper extends Thread {

    public Handler handler;
    private Handler mainHandler;

    public MyLooper(Handler mainThreadHandler) {
        mainHandler = mainThreadHandler;
    }

    @Override
    public void run() {

        Log.d("MyLooper", "run");

        Looper.prepare();

        handler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                String age = msg.getData().getString("AGE");
                String profession = msg.getData().getString("PROFESSION");
                Log.d("MyLooper get message: ", "Возраст: " + age + ", Профессия: " + profession);

                int ageInt = Integer.parseInt(age);

                android.os.SystemClock.sleep(ageInt * 1000);

                Message message = new Message();
                android.os.Bundle bundle = new android.os.Bundle();
                bundle.putString("result", String.format("Возраст: %s, Профессия: %s", age, profession));
                message.setData(bundle);
                mainHandler.sendMessage(message);
            }
        };

        Looper.loop();
    }
}
