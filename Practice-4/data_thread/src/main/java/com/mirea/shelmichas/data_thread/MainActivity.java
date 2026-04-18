package com.mirea.shelmichas.data_thread;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

import com.mirea.shelmichas.data_thread.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Создаётся 3 задачи (Runnable) для выполнения в UI-потоке
        final Runnable runn1 = () ->
                binding.tvInfo.setText(
                        "runOnUiThread - выполняется в UI-потоке сразу после добавления в очередь\n"
                );

        final Runnable runn2 = () ->
                binding.tvInfo.append(
                        "post - выполняется в UI-потоке через View\n"
                );

        final Runnable runn3 = () ->
                binding.tvInfo.append(
                        "postDelayed - выполняется в UI-потоке с задержкой\n\n" +
                                "Последовательность выполнения:\n" +
                                "1) runOnUiThread\n" +
                                "2) post\n" +
                                "3) postDelayed"
                );

        // запуск фонового потока
        Thread t = new Thread(() -> {
            try {

                TimeUnit.SECONDS.sleep(2);
                runOnUiThread(runn1);

                TimeUnit.SECONDS.sleep(1);
                binding.tvInfo.post(runn2);

                binding.tvInfo.postDelayed(runn3, 2000);

            } catch (InterruptedException ignored) {
            }
        });

        t.start();
    }
}