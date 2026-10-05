package com.mirea.shelmichas.nocturne.presentation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mirea.shelmichas.nocturne.domain.models.ClientInfo;
import com.mirea.shelmichas.nocturne.domain.models.Place;
import com.mirea.shelmichas.nocturne.domain.repository.PlaceRepository;
import com.mirea.shelmichas.nocturne.domain.usecase.GetClientInfoUseCase;
import com.mirea.shelmichas.nocturne.domain.usecase.GetPlacesUseCase;
import com.mirea.shelmichas.nocturne.domain.usecase.SaveClientInfoUseCase;
import com.mirea.shelmichas.nocturne.domain.usecase.SearchPlacesUseCase;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends ViewModel {

    private final PlaceRepository placeRepository;

    // Источники данных: локальная БД (Room) и сетевая часть (NetworkApi)
    private final MutableLiveData<List<Place>> roomPlacesSource = new MutableLiveData<>();
    private final MutableLiveData<List<Place>> networkPlacesSource = new MutableLiveData<>();

    // Объединяет данные из обоих источников
    private final MediatorLiveData<List<Place>> places = new MediatorLiveData<>();

    private final MutableLiveData<String> clientInfo = new MutableLiveData<>();

    private final MutableLiveData<String> searchStatus = new MutableLiveData<>();

    public MainViewModel(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
        places.addSource(roomPlacesSource, source -> mergeSources());
        places.addSource(networkPlacesSource, source -> mergeSources());
        loadPlaces();
        loadClientInfo();
    }

    public LiveData<List<Place>> getPlaces() {
        return places;
    }

    public LiveData<String> getClientInfo() {
        return clientInfo;
    }

    public LiveData<String> getSearchStatus() {
        return searchStatus;
    }

    // Данные из локальной базы (Room)
    public void loadPlaces() {
        roomPlacesSource.setValue(new GetPlacesUseCase(placeRepository).execute());
    }

    // Данные из сетевой части (NetworkApi с замоканными данными)
    public void searchPlaces(String query) {
        List<Place> found = new SearchPlacesUseCase(placeRepository).execute(query);
        networkPlacesSource.setValue(found);
        if (found.isEmpty()) {
            searchStatus.setValue("Сетевая часть: ничего не найдено по запросу \"" + query + "\"");
            return;
        }
        StringBuilder names = new StringBuilder();
        for (Place place : found) {
            if (names.length() > 0) {
                names.append(", ");
            }
            names.append(place.getName()).append(" (").append(place.getCity()).append(")");
        }
        searchStatus.setValue("Сетевая часть: найдено " + found.size() + " — " + names);
    }

    public void saveClientInfo(String name, String phone) {
        new SaveClientInfoUseCase(placeRepository).execute(new ClientInfo(name, phone));
        loadClientInfo();
    }

    private void loadClientInfo() {
        ClientInfo info = new GetClientInfoUseCase(placeRepository).execute();
        clientInfo.setValue("Инфо о клиенте: " + info.getName() + ", " + info.getPhone());
    }

    private void mergeSources() {
        List<Place> result = new ArrayList<>();
        if (roomPlacesSource.getValue() != null) {
            result.addAll(roomPlacesSource.getValue());
        }
        if (networkPlacesSource.getValue() != null) {
            result.addAll(networkPlacesSource.getValue());
        }
        places.setValue(result);
    }
}