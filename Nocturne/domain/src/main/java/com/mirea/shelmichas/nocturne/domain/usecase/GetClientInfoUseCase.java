package com.mirea.shelmichas.nocturne.domain.usecase;

import com.mirea.shelmichas.nocturne.domain.models.ClientInfo;
import com.mirea.shelmichas.nocturne.domain.repository.PlaceRepository;

public class GetClientInfoUseCase {
    private final PlaceRepository placeRepository;

    public GetClientInfoUseCase(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public ClientInfo execute() {
        return placeRepository.getClientInfo();
    }
}