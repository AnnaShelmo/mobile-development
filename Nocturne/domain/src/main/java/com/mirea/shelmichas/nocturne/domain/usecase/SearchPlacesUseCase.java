package com.mirea.shelmichas.nocturne.domain.usecase;

import com.mirea.shelmichas.nocturne.domain.models.Place;
import com.mirea.shelmichas.nocturne.domain.repository.PlaceRepository;

import java.util.List;

public class SearchPlacesUseCase {
    private final PlaceRepository placeRepository;

    public SearchPlacesUseCase(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public List<Place> execute(String query) {
        return placeRepository.searchPlaces(query);
    }
}