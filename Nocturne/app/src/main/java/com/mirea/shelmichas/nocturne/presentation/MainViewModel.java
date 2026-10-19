package com.mirea.shelmichas.nocturne.presentation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mirea.shelmichas.nocturne.domain.models.ClientInfo;
import com.mirea.shelmichas.nocturne.domain.repository.PlaceRepository;
import com.mirea.shelmichas.nocturne.domain.usecase.GetClientInfoUseCase;
import com.mirea.shelmichas.nocturne.domain.usecase.SaveClientInfoUseCase;

public class MainViewModel extends ViewModel {

    private final PlaceRepository placeRepository;

    private final MutableLiveData<String> clientInfo = new MutableLiveData<>();

    public MainViewModel(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
        loadClientInfo();
    }

    public LiveData<String> getClientInfo() {
        return clientInfo;
    }

    // Информация о клиенте (SharedPreferences)
    public void saveClientInfo(String name, String phone) {
        new SaveClientInfoUseCase(placeRepository).execute(new ClientInfo(name, phone));
        loadClientInfo();
    }

    private void loadClientInfo() {
        ClientInfo info = new GetClientInfoUseCase(placeRepository).execute();
        clientInfo.setValue("Инфо о клиенте: " + info.getName() + ", " + info.getPhone());
    }
}
