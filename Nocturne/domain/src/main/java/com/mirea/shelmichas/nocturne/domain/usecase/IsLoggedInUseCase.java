package com.mirea.shelmichas.nocturne.domain.usecase;

import com.mirea.shelmichas.nocturne.domain.repository.AuthRepository;

public class IsLoggedInUseCase {
    private final AuthRepository authRepository;

    public IsLoggedInUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public boolean execute() {
        return authRepository.isLoggedIn();
    }
}