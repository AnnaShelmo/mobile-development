package com.mirea.shelmichas.looper;

import android.os.Bundle;
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

        myLooper = new MyLooper();
        myLooper.start();

        binding.buttonStart.setOnClickListener(v -> {

            if (myLooper.handler != null) {
                Message msg = Message.obtain();
                msg.obj = "Сообщение из MainActivity";
                myLooper.handler.sendMessage(msg);
            }

        });
    }
}