package com.mirea.shelmichas.nocturne.presentation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mirea.shelmichas.nocturne.domain.models.Place;
import com.mirea.shelmichas.nocturne.domain.repository.AuthRepository;
import com.mirea.shelmichas.nocturne.domain.repository.PlaceRepository;
import com.mirea.shelmichas.nocturne.domain.usecase.GetFavoritePlacesUseCase;
import com.mirea.shelmichas.nocturne.domain.usecase.IsLoggedInUseCase;

import java.util.ArrayList;
import java.util.List;

// Экран избранного: доступен только авторизованному пользователю.
public class FavoritesViewModel extends ViewModel {

    private final PlaceRepository placeRepository;
    private final AuthRepository authRepository;

    private final MutableLiveData<List<Place>> favorites = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loggedIn = new MutableLiveData<>();

    public FavoritesViewModel(PlaceRepository placeRepository, AuthRepository authRepository) {
        this.placeRepository = placeRepository;
        this.authRepository = authRepository;
    }

    public LiveData<List<Place>> getFavorites() {
        return favorites;
    }

    public LiveData<Boolean> getLoggedIn() {
        return loggedIn;
    }

    public void load() {
        boolean isLoggedIn = new IsLoggedInUseCase(authRepository).execute();
        loggedIn.setValue(isLoggedIn);
        if (isLoggedIn) {
            favorites.setValue(new GetFavoritePlacesUseCase(placeRepository).execute());
        } else {
            favorites.setValue(new ArrayList<>());
        }
    }
}
