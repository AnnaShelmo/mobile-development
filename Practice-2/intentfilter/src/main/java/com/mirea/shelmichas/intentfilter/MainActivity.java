package com.mirea.shelmichas.intentfilter;

import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

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
    }

    public void openBrowser(View view) {
        Uri address = Uri.parse("https://www.mirea.ru/");
        Intent openLinkIntent = new Intent(Intent.ACTION_VIEW, address); //показать
        startActivity(openLinkIntent);
    }

    public void shareText(View view) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND); //отправить данные
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "MIREA"); //тема
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Шелмич Анна Славковна"); //основной текст
        startActivity(Intent.createChooser(shareIntent, "МОИ ФИО")); //выбор приложений
    }
}