package com.mirea.shelmichas.nocturne.domain.usecase;

import com.mirea.shelmichas.nocturne.domain.repository.PlaceRepository;

public class AddFavoriteUseCase {
    private final PlaceRepository placeRepository;

    public AddFavoriteUseCase(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public void execute(int placeId) {
        placeRepository.addFavorite(placeId);
    }
}
