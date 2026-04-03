package com.mirea.shelmichas.intentapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        textView = findViewById(R.id.textViewResult);

        // Получаем Intent
        Intent intent = getIntent();

        // Извлекаем переданное время
        String time = intent.getStringExtra(MainActivity.TIME_KEY);


        int numberInGroup = 30;
        int square = numberInGroup * numberInGroup;

        String result = "КВАДРАТ МОЕГО НОМЕРА ПО СПИСКУ В ГРУППЕ СОСТАВЛЯЕТ ЧИСЛО "
                + square + ", а текущее время " + time;

        textView.setText(result);
    }
}