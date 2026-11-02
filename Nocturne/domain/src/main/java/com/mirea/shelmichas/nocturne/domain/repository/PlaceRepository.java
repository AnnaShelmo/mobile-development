package com.mirea.shelmichas.nocturne.domain.repository;

import com.mirea.shelmichas.nocturne.domain.models.ClientInfo;
import com.mirea.shelmichas.nocturne.domain.models.Place;

import java.util.List;

public interface PlaceRepository {
    List<Place> getPlaces();

    List<Place> getSavedPlaces();

    Place getPlace(int id);

    List<Place> searchPlaces(String query);

    void savePlace(Place place);

    List<Place> getFavoritePlaces();

    boolean isFavorite(int placeId);

    void addFavorite(int placeId);

    void removeFavorite(int placeId);

    ClientInfo getClientInfo();

    void saveClientInfo(ClientInfo clientInfo);
}