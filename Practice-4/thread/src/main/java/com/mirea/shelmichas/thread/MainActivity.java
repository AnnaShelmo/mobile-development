package com.mirea.shelmichas.thread;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

import com.mirea.shelmichas.thread.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Thread mainThread = Thread.currentThread();

        binding.textViewInfo.setText(
                "Имя текущего потока: " + mainThread.getName()
        );

        // Меняем имя потока (впиши свои данные)
        mainThread.setName("ГРУППА: БСБО-08-23, НОМЕР ПО СПИСКУ: 30");

        binding.textViewInfo.append(
                "\nНовое имя потока: " + mainThread.getName()
        );

        Log.d(MainActivity.class.getSimpleName(),
                "Stack: " + Arrays.toString(mainThread.getStackTrace()));

        binding.buttonStart.setOnClickListener(v -> {

            new Thread(new Runnable() {
                @Override
                public void run() {

                    Log.d("ThreadProject", "Поток запущен");

                    long endTime = System.currentTimeMillis() + 20 * 1000;

                    while (System.currentTimeMillis() < endTime) {
                        synchronized (this) {
                            try {
                                wait(endTime - System.currentTimeMillis());
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    Log.d("ThreadProject", "Поток завершён");
                }
            }).start();

        });
    }
}