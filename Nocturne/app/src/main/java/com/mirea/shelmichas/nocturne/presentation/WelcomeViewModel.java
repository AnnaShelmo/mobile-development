package com.mirea.shelmichas.nocturne.presentation;

import androidx.lifecycle.ViewModel;

import com.mirea.shelmichas.nocturne.domain.repository.AuthRepository;
import com.mirea.shelmichas.nocturne.domain.usecase.IsLoggedInUseCase;
import com.mirea.shelmichas.nocturne.domain.usecase.SignOutUseCase;

// Логика стартового экрана: гостевой режим = выход из аккаунта.
public class WelcomeViewModel extends ViewModel {

    private final AuthRepository authRepository;

    public WelcomeViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public boolean isLoggedIn() {
        return new IsLoggedInUseCase(authRepository).execute();
    }

    public void enterGuestMode() {
        new SignOutUseCase(authRepository).execute();
    }
}
