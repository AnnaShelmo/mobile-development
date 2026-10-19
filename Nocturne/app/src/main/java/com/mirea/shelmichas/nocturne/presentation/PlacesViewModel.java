package com.mirea.shelmichas.nocturne.presentation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mirea.shelmichas.nocturne.domain.models.Place;
import com.mirea.shelmichas.nocturne.domain.repository.PlaceRepository;
import com.mirea.shelmichas.nocturne.domain.usecase.GetPlacesUseCase;
import com.mirea.shelmichas.nocturne.domain.usecase.GetSavedPlacesUseCase;
import com.mirea.shelmichas.nocturne.domain.usecase.SearchPlacesUseCase;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PlacesViewModel extends ViewModel {

    private final PlaceRepository placeRepository;

    // Два источника каталога: замоканная сетевая часть и локальная БД (Room)
    private final MutableLiveData<List<Place>> networkSource = new MutableLiveData<>();
    private final MutableLiveData<List<Place>> databaseSource = new MutableLiveData<>();

    // Объединяет данные из обоих источников
    private final MediatorLiveData<List<Place>> places = new MediatorLiveData<>();

    private final MutableLiveData<String> searchStatus = new MutableLiveData<>();

    // в режиме поиска в списке остаются только результаты поиска
    private boolean searchActive = false;

    public PlacesViewModel(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
        places.addSource(networkSource, source -> mergeSources()); //подключение
        places.addSource(databaseSource, source -> mergeSources()); //подключение
        loadPlaces();
    }

    public LiveData<List<Place>> getPlaces() {
        return places;
    }

    public LiveData<String> getSearchStatus() {
        return searchStatus;
    }

    // Полный каталог: сервис + сохранённое в БД
    public void loadPlaces() {
        networkSource.setValue(new GetPlacesUseCase(placeRepository).execute());
        databaseSource.setValue(new GetSavedPlacesUseCase(placeRepository).execute());
    }

    // Поиск по замоканной сетевой части: результаты заменяют каталог
    public void searchPlaces(String query) {
        if (query == null || query.trim().isEmpty()) {
            resetSearch();
            return;
        }
        List<Place> found = new SearchPlacesUseCase(placeRepository).execute(query);
        searchActive = true;
        networkSource.setValue(found);
        if (found.isEmpty()) {
            searchStatus.setValue("Ничего не найдено по запросу \"" + query + "\"");
            return;
        }
        StringBuilder names = new StringBuilder();
        for (Place place : found) {
            if (names.length() > 0) {
                names.append(", ");
            }
            names.append(place.getName()).append(" (").append(place.getCity()).append(")");
        }
        searchStatus.setValue("Найдено " + found.size() + " — " + names);
    }

    // Возврат к полному каталогу
    public void resetSearch() {
        searchActive = false;
        loadPlaces();
        searchStatus.setValue("");
    }

    private void mergeSources() {
        // одно и то же место может прийти и из сервиса, и из БД, поэтому убираем дубликаты
        Map<String, Place> merged = new LinkedHashMap<>();
        if (networkSource.getValue() != null) {
            for (Place place : networkSource.getValue()) {
                merged.put(place.getName(), place);
            }
        }
        // данные из БД добавляем только к каталогу, а не к результатам поиска
        if (!searchActive && databaseSource.getValue() != null) {
            for (Place place : databaseSource.getValue()) {
                merged.putIfAbsent(place.getName(), place);
            }
        }
        places.setValue(new ArrayList<>(merged.values()));
    }
}
