package com.mirea.shelmichas.nocturne.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.mirea.shelmichas.nocturne.R;
import com.mirea.shelmichas.nocturne.domain.models.Place;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MainViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textViewPlaces = findViewById(R.id.textViewPlaces);
        TextView textViewClientInfo = findViewById(R.id.textViewClientInfo);
        TextView textViewSearchStatus = findViewById(R.id.textViewSearchStatus);
        EditText fieldSearch = findViewById(R.id.fieldSearch);
        EditText fieldClientName = findViewById(R.id.fieldClientName);
        EditText fieldClientPhone = findViewById(R.id.fieldClientPhone);

        vm = new ViewModelProvider(this, new NocturneViewModelFactory(this)).get(MainViewModel.class);

        // Подписка на объединённые данные из Room и NetworkApi
        vm.getPlaces().observe(this, places -> {
            StringBuilder builder = new StringBuilder();
            if (places != null) {
                for (Place place : places) {
                    builder.append(place.getName()).append("\n");
                }
            }
            textViewPlaces.setText(builder.toString());
        });

        vm.getClientInfo().observe(this, textViewClientInfo::setText);

        vm.getSearchStatus().observe(this, textViewSearchStatus::setText);

        findViewById(R.id.buttonSearch).setOnClickListener(v ->
                vm.searchPlaces(fieldSearch.getText().toString().trim()));

        findViewById(R.id.buttonSaveClient).setOnClickListener(v ->
                vm.saveClientInfo(fieldClientName.getText().toString().trim(),
                        fieldClientPhone.getText().toString().trim()));

        findViewById(R.id.buttonLogin).setOnClickListener(v ->
                startActivity(new Intent(this, LoginActivity.class)));
    }
}