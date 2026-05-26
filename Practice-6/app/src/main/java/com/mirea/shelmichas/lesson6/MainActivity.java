package com.mirea.shelmichas.lesson6;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editGroup;
    private EditText editNumber;
    private EditText editFilm;
    
    private static final String PREFS_NAME = "mirea_settings";
    private static final String KEY_GROUP = "GROUP";
    private static final String KEY_NUMBER = "NUMBER";
    private static final String KEY_FILM = "FILM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editGroup = findViewById(R.id.editGroup);
        editNumber = findViewById(R.id.editNumber);
        editFilm = findViewById(R.id.editFilm);
        Button btnSave = findViewById(R.id.btnSave);

        // Загружаем сохранённые данные при запуске
        loadData();

        // Сохраняем при нажатии кнопки
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }

    private void saveData() {
        SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(KEY_GROUP, editGroup.getText().toString());
        editor.putString(KEY_NUMBER, editNumber.getText().toString());
        editor.putString(KEY_FILM, editFilm.getText().toString());

        editor.apply();

        Toast.makeText(this, "Сохранено!", Toast.LENGTH_SHORT).show();
    }

    private void loadData() {
        SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        String group = sharedPref.getString(KEY_GROUP, "");
        String number = sharedPref.getString(KEY_NUMBER, "");
        String film = sharedPref.getString(KEY_FILM, "");

        editGroup.setText(group);
        editNumber.setText(number);
        editFilm.setText(film);
    }
}