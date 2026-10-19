package com.mirea.shelmichas.nocturne.presentation;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.mirea.shelmichas.nocturne.data.network.NetworkApi;
import com.mirea.shelmichas.nocturne.data.repository.AuthRepositoryImpl;
import com.mirea.shelmichas.nocturne.data.repository.PlaceRepositoryImpl;
import com.mirea.shelmichas.nocturne.data.storage.PlaceStorage;
import com.mirea.shelmichas.nocturne.data.storage.room.AppDatabase;
import com.mirea.shelmichas.nocturne.data.storage.room.RoomPlaceStorage;
import com.mirea.shelmichas.nocturne.data.storage.sharedprefs.ClientStorage;
import com.mirea.shelmichas.nocturne.data.storage.sharedprefs.SharedPrefClientStorage;
import com.mirea.shelmichas.nocturne.domain.repository.AuthRepository;
import com.mirea.shelmichas.nocturne.domain.repository.PlaceRepository;

public class NocturneViewModelFactory implements ViewModelProvider.Factory {

    private final Context context; // Activity передаётся сюда, но в ViewModel не попадает

    public NocturneViewModelFactory(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(createPlaceRepository()); // ← ViewModel получает только репозиторий
        }
        if (modelClass.isAssignableFrom(PlacesViewModel.class)) {
            return (T) new PlacesViewModel(createPlaceRepository()); // ← каталог: сервис + БД
        }
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            AuthRepository authRepository = new AuthRepositoryImpl();
            return (T) new LoginViewModel(authRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }

    private PlaceRepository createPlaceRepository() {
        PlaceStorage placeStorage = new RoomPlaceStorage(AppDatabase.getInstance(context).placeDao());
        ClientStorage clientStorage = new SharedPrefClientStorage(context);
        return new PlaceRepositoryImpl(placeStorage, clientStorage, new NetworkApi()); // ← composition root
    }
}