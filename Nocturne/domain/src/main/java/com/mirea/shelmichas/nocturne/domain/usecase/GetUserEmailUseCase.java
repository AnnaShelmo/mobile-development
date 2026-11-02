package com.mirea.shelmichas.nocturne.domain.usecase;

import com.mirea.shelmichas.nocturne.domain.repository.AuthRepository;

public class GetUserEmailUseCase {
    private final AuthRepository authRepository;

    public GetUserEmailUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public String execute() {
        return authRepository.getCurrentUserEmail();
    }
}
