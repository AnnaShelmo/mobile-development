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

    private final Context context;

    public NocturneViewModelFactory(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            PlaceStorage placeStorage = new RoomPlaceStorage(AppDatabase.getInstance(context).placeDao());
            ClientStorage clientStorage = new SharedPrefClientStorage(context);
            PlaceRepository placeRepository =
                    new PlaceRepositoryImpl(placeStorage, clientStorage, new NetworkApi());
            return (T) new MainViewModel(placeRepository);
        }
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            AuthRepository authRepository = new AuthRepositoryImpl();
            return (T) new LoginViewModel(authRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}