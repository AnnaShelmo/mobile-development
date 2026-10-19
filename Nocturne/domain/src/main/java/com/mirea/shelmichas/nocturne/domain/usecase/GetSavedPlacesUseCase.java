package com.mirea.shelmichas.nocturne.domain.usecase;

import com.mirea.shelmichas.nocturne.domain.models.Place;
import com.mirea.shelmichas.nocturne.domain.repository.PlaceRepository;

import java.util.List;

public class GetSavedPlacesUseCase {
    private final PlaceRepository placeRepository;

    public GetSavedPlacesUseCase(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public List<Place> execute() {
        return placeRepository.getSavedPlaces();
    }
}
