package com.mirea.shelmichas.nocturne.domain.usecase;

import com.mirea.shelmichas.nocturne.domain.models.ClientInfo;
import com.mirea.shelmichas.nocturne.domain.repository.PlaceRepository;

public class SaveClientInfoUseCase {
    private final PlaceRepository placeRepository;

    public SaveClientInfoUseCase(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public void execute(ClientInfo clientInfo) {
        placeRepository.saveClientInfo(clientInfo);
    }
}