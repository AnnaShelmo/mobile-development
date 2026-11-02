package com.mirea.shelmichas.nocturne.presentation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mirea.shelmichas.nocturne.domain.repository.AuthCallback;
import com.mirea.shelmichas.nocturne.domain.repository.AuthRepository;
import com.mirea.shelmichas.nocturne.domain.usecase.IsLoggedInUseCase;
import com.mirea.shelmichas.nocturne.domain.usecase.SignInUseCase;
import com.mirea.shelmichas.nocturne.domain.usecase.SignOutUseCase;
import com.mirea.shelmichas.nocturne.domain.usecase.SignUpUseCase;

public class LoginViewModel extends ViewModel {

    private final AuthRepository authRepository;

    private final MutableLiveData<String> authStatus = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loggedIn = new MutableLiveData<>();
    // Становится true только после успешного входа/регистрации.
    private final MutableLiveData<Boolean> authCompleted = new MutableLiveData<>(false);

    public LoginViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;
        updateAuthStatus();
    }

    public LiveData<String> getAuthStatus() {
        return authStatus;
    }

    public LiveData<Boolean> getLoggedIn() {
        return loggedIn;
    }

    public LiveData<Boolean> getAuthCompleted() {
        return authCompleted;
    }

    public void consumeAuthCompleted() {
        authCompleted.setValue(false);
    }

    public void signIn(String email, String password) {
        new SignInUseCase(authRepository).execute(email, password, new AuthCallback() {
            @Override
            public void onSuccess() {
                updateAuthStatus();
                authCompleted.setValue(true);
            }

            @Override
            public void onFailure(String message) {
                authStatus.setValue("Ошибка входа: " + message);
            }
        });
    }

    public void signUp(String email, String password) {
        new SignUpUseCase(authRepository).execute(email, password, new AuthCallback() {
            @Override
            public void onSuccess() {
                updateAuthStatus();
                authCompleted.setValue(true);
            }

            @Override
            public void onFailure(String message) {
                authStatus.setValue("Ошибка регистрации: " + message);
            }
        });
    }

    public void signOut() {
        new SignOutUseCase(authRepository).execute();
        updateAuthStatus();
    }

    private void updateAuthStatus() {
        boolean isLoggedIn = new IsLoggedInUseCase(authRepository).execute();
        loggedIn.setValue(isLoggedIn);
        authStatus.setValue(isLoggedIn ? "Статус: авторизован" : "Статус: не авторизован");
    }
}