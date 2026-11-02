package com.mirea.shelmichas.nocturne.domain.usecase;

import com.mirea.shelmichas.nocturne.domain.repository.PlaceRepository;

public class RemoveFavoriteUseCase {
    private final PlaceRepository placeRepository;

    public RemoveFavoriteUseCase(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public void execute(int placeId) {
        placeRepository.removeFavorite(placeId);
    }
}
