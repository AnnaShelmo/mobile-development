package com.mirea.shelmichas.nocturne.domain.usecase;

import com.mirea.shelmichas.nocturne.domain.models.Place;
import com.mirea.shelmichas.nocturne.domain.repository.PlaceRepository;

public class GetPlaceUseCase {
    private final PlaceRepository placeRepository;

    public GetPlaceUseCase(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public Place execute(int id) {
        return placeRepository.getPlace(id);
    }
}