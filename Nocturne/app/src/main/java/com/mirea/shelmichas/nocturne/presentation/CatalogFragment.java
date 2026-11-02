package com.mirea.shelmichas.nocturne.presentation;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mirea.shelmichas.nocturne.R;

// Каталог мест: список с поиском.
public class CatalogFragment extends Fragment {

    private PlacesViewModel vm;

    public CatalogFragment() {
        super(R.layout.fragment_catalog);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText fieldSearch = view.findViewById(R.id.fieldSearch);
        TextView searchStatus = view.findViewById(R.id.textViewSearchStatus);

        vm = new ViewModelProvider(this, new NocturneViewModelFactory(requireContext()))
                .get(PlacesViewModel.class);

        // Нажатие на карточку открывает страницу места во фрагменте.
        PlaceRecyclerViewAdapter placeAdapter = new PlaceRecyclerViewAdapter(place ->
                ((MainActivity) requireActivity()).openPlaceDetails(place.getId()));

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewPlaces);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(placeAdapter);

        // Подписка на каталог: замоканная сетевая часть + локальная БД
        vm.getPlaces().observe(getViewLifecycleOwner(), placeAdapter::setItems);
        vm.getSearchStatus().observe(getViewLifecycleOwner(), searchStatus::setText);

        view.findViewById(R.id.buttonSearch).setOnClickListener(v ->
                vm.searchPlaces(fieldSearch.getText().toString().trim()));

        view.findViewById(R.id.buttonReset).setOnClickListener(v -> {
            fieldSearch.setText("");
            vm.resetSearch();
        });
    }
}
