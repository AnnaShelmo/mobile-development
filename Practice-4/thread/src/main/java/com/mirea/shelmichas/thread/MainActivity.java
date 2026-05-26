package com.mirea.shelmichas.thread;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

import com.mirea.shelmichas.thread.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // создаётся объект binding из XML-файла
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Получение текущего (главный) потока
        Thread mainThread = Thread.currentThread();

        binding.textViewInfo.setText(
                "Имя текущего потока: " + mainThread.getName()
        );

        mainThread.setName("ГРУППА: БСБО-08-23, НОМЕР ПО СПИСКУ: 30");

        binding.textViewInfo.append(
                "\nНовое имя потока: " + mainThread.getName()
        );

        Log.d(MainActivity.class.getSimpleName(),
                "Stack: " + Arrays.toString(mainThread.getStackTrace()));

        binding.buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        int numberThread = counter++;
                        Log.d("ThreadProject", String.format("Запущен поток № %d студентом группы № БСБО-08-23 номер по списку № 30", numberThread));
                        long endTime = System.currentTimeMillis() + 20 * 1000; // Имитация долгой работы — 20 секунд
                        while (System.currentTimeMillis() < endTime) {
                            synchronized (this) {
                                try {
                                    wait(endTime - System.currentTimeMillis());
                                    Log.d(MainActivity.class.getSimpleName(), "Endtime: " + endTime);
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                        Log.d("ThreadProject", "Выполнен поток № " + numberThread);

                        // подсчёт среднего
                        int pairs = Integer.parseInt(binding.editTextPairs.getText().toString());
                        int days = Integer.parseInt(binding.editTextDays.getText().toString());
                        double average = (double) pairs / days;
                        binding.textViewResult.post(() -> {
                            binding.textViewResult.setText("Среднее: " + average);
                        });
                    }
                }).start();
            }
        });
    }
}
