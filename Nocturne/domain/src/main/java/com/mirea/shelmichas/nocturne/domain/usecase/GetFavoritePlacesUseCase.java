package com.mirea.shelmichas.nocturne.domain.usecase;

import com.mirea.shelmichas.nocturne.domain.models.Place;
import com.mirea.shelmichas.nocturne.domain.repository.PlaceRepository;

import java.util.List;

public class GetFavoritePlacesUseCase {
    private final PlaceRepository placeRepository;

    public GetFavoritePlacesUseCase(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public List<Place> execute() {
        return placeRepository.getFavoritePlaces();
    }
}
