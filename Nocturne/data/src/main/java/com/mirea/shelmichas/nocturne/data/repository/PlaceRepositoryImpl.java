package com.mirea.shelmichas.nocturne.data.repository;

import com.mirea.shelmichas.nocturne.data.network.NetworkApi;
import com.mirea.shelmichas.nocturne.data.storage.PlaceStorage;
import com.mirea.shelmichas.nocturne.data.storage.sharedprefs.ClientStorage;
import com.mirea.shelmichas.nocturne.data.storage.sharedprefs.models.Client;
import com.mirea.shelmichas.nocturne.domain.models.ClientInfo;
import com.mirea.shelmichas.nocturne.domain.models.Place;
import com.mirea.shelmichas.nocturne.domain.repository.PlaceRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PlaceRepositoryImpl implements PlaceRepository {

    private final PlaceStorage placeStorage;
    private final ClientStorage clientStorage;
    private final NetworkApi networkApi;

    public PlaceRepositoryImpl(PlaceStorage placeStorage, ClientStorage clientStorage,
                               NetworkApi networkApi) {
        this.placeStorage = placeStorage;
        this.clientStorage = clientStorage;
        this.networkApi = networkApi;
    }

    @Override
    public List<Place> getPlaces() {
        List<Place> result = new ArrayList<>();
        for (com.mirea.shelmichas.nocturne.data.storage.models.Place place : placeStorage.getPlaces()) {
            result.add(mapToDomain(place));
        }
        return result;
    }

    @Override
    public Place getPlace(int id) {
        return mapToDomain(placeStorage.getPlace(id));
    }

    // Поиск выполняется через сетевой сервис (NetworkApi с замоканными данными)
    @Override
    public List<Place> searchPlaces(String query) {
        List<Place> result = new ArrayList<>();
        for (com.mirea.shelmichas.nocturne.data.storage.models.Place place : networkApi.searchPlaces(query)) {
            result.add(mapToDomain(place));
        }
        return result;
    }

    // Сохранение данных в БД (Room)
    @Override
    public void savePlace(Place place) {
        placeStorage.savePlace(mapToStorage(place));
    }

    // Информация о клиенте хранится в SharedPreferences
    @Override
    public ClientInfo getClientInfo() {
        Client client = clientStorage.get();
        return new ClientInfo(client.getName(), client.getPhone());
    }

    @Override
    public void saveClientInfo(ClientInfo clientInfo) {
        clientStorage.save(new Client(clientInfo.getName(), clientInfo.getPhone()));
    }

    // преобразование моделей
    private Place mapToDomain(com.mirea.shelmichas.nocturne.data.storage.models.Place place) {
        return new Place(place.getId(), place.getName(), place.getDescription(), place.getCity(),
                place.getCountry(), place.getCategory(), place.getImageUrl(), place.getYear());
    }

    private com.mirea.shelmichas.nocturne.data.storage.models.Place mapToStorage(Place place) {
        return new com.mirea.shelmichas.nocturne.data.storage.models.Place(
                place.getId(), place.getName(), place.getDescription(), place.getCity(),
                place.getCountry(), place.getCategory(), place.getImageUrl(), place.getYear(),
                LocalDate.now().toString());
    }
}