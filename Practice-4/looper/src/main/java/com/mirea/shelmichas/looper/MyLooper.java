package com.mirea.shelmichas.looper;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

public class MyLooper extends Thread { // создаем свой поток

    public Handler handler; // обработчик внутри нашего потока
    private Handler mainHandler; // обработчик главного потока (UI)

    public MyLooper(Handler mainThreadHandler) { // Передаём mainHandler чтобы отправлять результат обратно в UI
        mainHandler = mainThreadHandler;
    }

    @Override
    public void run() { // Запускаем поток, создаём очередь сообщений.

        Log.d("MyLooper", "run");

        Looper.prepare();

        handler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) { // когда приходит сообщение
                String age = msg.getData().getString("AGE");
                String profession = msg.getData().getString("PROFESSION");
                Log.d("MyLooper get message: ", "Возраст: " + age + ", Профессия: " + profession);

                int ageInt = Integer.parseInt(age);

                android.os.SystemClock.sleep(ageInt * 1000);

                Message message = new Message();
                android.os.Bundle bundle = new android.os.Bundle();
                bundle.putString("result", String.format("Возраст: %s, Профессия: %s", age, profession));
                message.setData(bundle);
                mainHandler.sendMessage(message); // отправляем результат в главный поток через mainHandler
            }
        };

        Looper.loop(); // Запускаем бесконечный цикл — поток работает пока не остановим
    }
}
