package com.mirea.shelmichas.intentapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static final String TIME_KEY = "time_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickSendTime(View view) {

        // Получаем системное время
        long dateInMillis = System.currentTimeMillis();

        String format = "yyyy-MM-dd HH:mm:ss";
        final SimpleDateFormat sdf = new SimpleDateFormat(format);
        String dateString = sdf.format(new Date(dateInMillis));

        // Создаём Intent
        Intent intent = new Intent(this, SecondActivity.class);

        // Передаём данные
        intent.putExtra(TIME_KEY, dateString);

        // Запускаем вторую Activity
        startActivity(intent);
    }
}