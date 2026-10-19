package com.mirea.shelmichas.nocturne.presentation;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mirea.shelmichas.nocturne.R;

public class PlacesActivity extends AppCompatActivity {

    private PlacesViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);

        EditText fieldSearch = findViewById(R.id.fieldSearch);
        TextView searchStatus = findViewById(R.id.textViewSearchStatus);

        vm = new ViewModelProvider(this, new NocturneViewModelFactory(this)).get(PlacesViewModel.class);

        PlaceRecyclerViewAdapter placeAdapter = new PlaceRecyclerViewAdapter();
        RecyclerView recyclerView = findViewById(R.id.recyclerViewPlaces);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(placeAdapter);

        // Подписка на каталог: замоканная сетевая часть + локальная БД
        vm.getPlaces().observe(this, placeAdapter::setItems);
        vm.getSearchStatus().observe(this, searchStatus::setText);

        findViewById(R.id.buttonSearch).setOnClickListener(v ->
                vm.searchPlaces(fieldSearch.getText().toString().trim()));

        findViewById(R.id.buttonReset).setOnClickListener(v -> {
            fieldSearch.setText("");
            vm.resetSearch();
        });
    }
}
