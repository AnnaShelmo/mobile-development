package com.mirea.shelmichas.nocturne.domain.usecase;

import com.mirea.shelmichas.nocturne.domain.repository.AuthRepository;

public class SignOutUseCase {
    private final AuthRepository authRepository;

    public SignOutUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public void execute() {
        authRepository.signOut();
    }
}