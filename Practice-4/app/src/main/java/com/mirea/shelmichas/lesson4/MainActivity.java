package com.mirea.shelmichas.lesson4;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.mirea.shelmichas.lesson4.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // создаётся объект binding из XML-файла
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // номер по списку в поле названия песни
        binding.textSongTitle.setText("Мой номер по списку №30");

        // Обработчик кнопки
        binding.buttonPlay.setOnClickListener(v ->
                Log.d(MainActivity.class.getSimpleName(), "buttonPlay clicked")
        );
    }
}