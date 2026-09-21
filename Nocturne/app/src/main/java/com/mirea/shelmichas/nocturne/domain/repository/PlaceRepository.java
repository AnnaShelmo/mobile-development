package com.mirea.shelmichas.nocturne.domain.repository;

import com.mirea.shelmichas.nocturne.domain.models.Place;

import java.util.List;

public interface PlaceRepository {
    List<Place> getPlaces();
    Place getPlace(int id);
    List<Place> searchPlaces(String query);
}