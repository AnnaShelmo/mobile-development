package com.mirea.shelmichas.nocturne.domain.usecase;

import com.mirea.shelmichas.nocturne.domain.repository.AuthCallback;
import com.mirea.shelmichas.nocturne.domain.repository.AuthRepository;

public class SignUpUseCase {
    private final AuthRepository authRepository;

    public SignUpUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public void execute(String email, String password, AuthCallback callback) {
        authRepository.signUp(email, password, callback);
    }
}