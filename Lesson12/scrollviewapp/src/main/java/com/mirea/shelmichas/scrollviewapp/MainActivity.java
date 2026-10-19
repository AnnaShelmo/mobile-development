package com.mirea.shelmichas.scrollviewapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.math.BigInteger;

public class MainActivity extends AppCompatActivity {

    // сколько элементов прогрессии показываем
    private static final int ELEMENT_COUNT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        LinearLayout wrapper = findViewById(R.id.wrapper); // находим по id
        LayoutInflater inflater = LayoutInflater.from(this); // Берём сервис LayoutInflater, привязанный к этой Activity. Он умеет превращать XML-файл разметки в объекты View

        // геометрическая прогрессия со знаменателем 2: 1, 2, 4, 8, ...
        BigInteger element = BigInteger.ONE;
        for (int i = 1; i <= ELEMENT_COUNT; i++) {
            View item = inflater.inflate(R.layout.item, wrapper, false); // одной строки из item.xml
            TextView textView = item.findViewById(R.id.textView);
            textView.setText(i + ") " + element);
            wrapper.addView(item); // Вешаем готовую строку в контейнер
            element = element.multiply(BigInteger.valueOf(2));
        }
    }
}
