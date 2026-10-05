package com.mirea.shelmichas.nocturne.domain.usecase;

import com.mirea.shelmichas.nocturne.domain.models.Place;
import com.mirea.shelmichas.nocturne.domain.repository.PlaceRepository;

public class SavePlaceUseCase {
    private final PlaceRepository placeRepository;

    public SavePlaceUseCase(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public void execute(Place place) {
        placeRepository.savePlace(place);
    }
}