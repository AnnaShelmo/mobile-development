package com.mirea.shelmichas.nocturne.presentation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mirea.shelmichas.nocturne.domain.models.ClientInfo;
import com.mirea.shelmichas.nocturne.domain.repository.AuthRepository;
import com.mirea.shelmichas.nocturne.domain.repository.PlaceRepository;
import com.mirea.shelmichas.nocturne.domain.usecase.GetClientInfoUseCase;
import com.mirea.shelmichas.nocturne.domain.usecase.GetUserEmailUseCase;
import com.mirea.shelmichas.nocturne.domain.usecase.IsLoggedInUseCase;
import com.mirea.shelmichas.nocturne.domain.usecase.SaveClientInfoUseCase;
import com.mirea.shelmichas.nocturne.domain.usecase.SignOutUseCase;

// Экран профиля: статус входа, email и данные клиента.
public class ProfileViewModel extends ViewModel {

    private final AuthRepository authRepository;
    private final PlaceRepository placeRepository;

    private final MutableLiveData<Boolean> loggedIn = new MutableLiveData<>();
    private final MutableLiveData<String> email = new MutableLiveData<>();
    private final MutableLiveData<String> name = new MutableLiveData<>();
    private final MutableLiveData<String> phone = new MutableLiveData<>();

    public ProfileViewModel(AuthRepository authRepository, PlaceRepository placeRepository) {
        this.authRepository = authRepository;
        this.placeRepository = placeRepository;
        refresh();
    }

    public LiveData<Boolean> getLoggedIn() {
        return loggedIn;
    }

    public LiveData<String> getEmail() {
        return email;
    }

    public LiveData<String> getName() {
        return name;
    }

    public LiveData<String> getPhone() {
        return phone;
    }

    public void refresh() {
        loggedIn.setValue(new IsLoggedInUseCase(authRepository).execute());
        email.setValue(new GetUserEmailUseCase(authRepository).execute());

        ClientInfo info = new GetClientInfoUseCase(placeRepository).execute();
        name.setValue(info.getName());
        phone.setValue(info.getPhone());
    }

    public void saveClientInfo(String clientName, String clientPhone) {
        new SaveClientInfoUseCase(placeRepository)
                .execute(new ClientInfo(clientName, clientPhone));
        refresh();
    }

    public void signOut() {
        new SignOutUseCase(authRepository).execute();
        refresh();
    }
}
