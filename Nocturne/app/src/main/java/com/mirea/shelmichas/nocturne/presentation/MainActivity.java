package com.mirea.shelmichas.nocturne.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.mirea.shelmichas.nocturne.R;

public class MainActivity extends AppCompatActivity {

    private MainViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textViewClientInfo = findViewById(R.id.textViewClientInfo);
        EditText fieldClientName = findViewById(R.id.fieldClientName);
        EditText fieldClientPhone = findViewById(R.id.fieldClientPhone);

        vm = new ViewModelProvider(this, new NocturneViewModelFactory(this)).get(MainViewModel.class);

        vm.getClientInfo().observe(this, textViewClientInfo::setText);

        // Вход в приложение
        findViewById(R.id.buttonLogin).setOnClickListener(v ->
                startActivity(new Intent(this, LoginActivity.class)));

        // Продолжить как гость — сразу в каталог
        findViewById(R.id.textGuest).setOnClickListener(v ->
                startActivity(new Intent(this, PlacesActivity.class)));

        findViewById(R.id.buttonSaveClient).setOnClickListener(v ->
                vm.saveClientInfo(fieldClientName.getText().toString().trim(),
                        fieldClientPhone.getText().toString().trim()));
    }
}
