package com.mirea.shelmichas.nocturne.domain.usecase;

import com.mirea.shelmichas.nocturne.domain.repository.PlaceRepository;

public class IsFavoriteUseCase {
    private final PlaceRepository placeRepository;

    public IsFavoriteUseCase(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public boolean execute(int placeId) {
        return placeRepository.isFavorite(placeId);
    }
}
