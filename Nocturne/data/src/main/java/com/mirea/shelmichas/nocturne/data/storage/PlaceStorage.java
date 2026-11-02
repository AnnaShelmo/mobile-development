package com.mirea.shelmichas.nocturne.data.storage;

import com.mirea.shelmichas.nocturne.data.storage.models.Place;

import java.util.List;

public interface PlaceStorage {
    List<Place> getPlaces();
    Place getPlace(int id);
    List<Place> searchPlaces(String query);
    boolean savePlace(Place place);

    // Избранное хранится отдельно от таблицы мест.
    List<Integer> getFavoriteIds();
    boolean isFavorite(int placeId);
    boolean addFavorite(int placeId);
    boolean removeFavorite(int placeId);
}
