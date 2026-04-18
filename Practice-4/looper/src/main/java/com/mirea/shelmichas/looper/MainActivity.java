package com.mirea.shelmichas.looper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.appcompat.app.AppCompatActivity;

import com.mirea.shelmichas.looper.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MyLooper myLooper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Создаём обработчик для главного потока. Сюда приходит результат из MyLooper.
        Handler mainThreadHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                android.util.Log.d(MainActivity.class.getSimpleName(), "Task execute. This is result: " + msg.getData().getString("result"));
            }
        };

        // Создаём и запускаем MyLooper
        myLooper = new MyLooper(mainThreadHandler);
        myLooper.start();

        // создаём сообщение с данными
        // отправляем в MyLooper через handler
        binding.buttonStart.setOnClickListener(v -> {
            Message msg = Message.obtain();
            Bundle bundle = new Bundle();
            bundle.putString("AGE", binding.editTextAge.getText().toString());
            bundle.putString("PROFESSION", binding.editTextProfession.getText().toString());
            msg.setData(bundle);
            myLooper.handler.sendMessage(msg);
        });
    }
}
