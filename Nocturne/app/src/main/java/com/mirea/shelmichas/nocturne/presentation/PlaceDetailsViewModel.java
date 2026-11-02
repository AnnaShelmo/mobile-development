package com.mirea.shelmichas.nocturne.presentation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mirea.shelmichas.nocturne.domain.models.Place;
import com.mirea.shelmichas.nocturne.domain.models.Weather;
import com.mirea.shelmichas.nocturne.domain.repository.AuthRepository;
import com.mirea.shelmichas.nocturne.domain.repository.PlaceRepository;
import com.mirea.shelmichas.nocturne.domain.repository.WeatherRepository;
import com.mirea.shelmichas.nocturne.domain.usecase.AddFavoriteUseCase;
import com.mirea.shelmichas.nocturne.domain.usecase.GetWeatherUseCase;
import com.mirea.shelmichas.nocturne.domain.usecase.IsFavoriteUseCase;
import com.mirea.shelmichas.nocturne.domain.usecase.IsLoggedInUseCase;
import com.mirea.shelmichas.nocturne.domain.usecase.RemoveFavoriteUseCase;

public class PlaceDetailsViewModel extends ViewModel {

    private final PlaceRepository placeRepository;
    private final WeatherRepository weatherRepository;
    private final AuthRepository authRepository;

    private final MutableLiveData<Place> place = new MutableLiveData<>();
    private final MutableLiveData<Weather> weather = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    // Состояние избранного для открытого места.
    private final MutableLiveData<Boolean> favorite = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> loggedIn = new MutableLiveData<>(false);

    private int placeId = -1;

    public PlaceDetailsViewModel(PlaceRepository placeRepository,
                                 WeatherRepository weatherRepository,
                                 AuthRepository authRepository) {
        this.placeRepository = placeRepository;
        this.weatherRepository = weatherRepository;
        this.authRepository = authRepository;
    }

    public LiveData<Place> getPlace() {
        return place;
    }

    public LiveData<Weather> getWeather() {
        return weather;
    }

    public LiveData<String> getError() {
        return error;
    }

    public LiveData<Boolean> getFavorite() {
        return favorite;
    }

    public LiveData<Boolean> getLoggedIn() {
        return loggedIn;
    }

    // Загрузка места по id и погоды для его города
    public void load(int id) {
        placeId = id;
        place.setValue(placeRepository.getPlace(id));
        Place current = place.getValue();
        if (current == null) {
            error.setValue("Место не найдено");
            return;
        }

        loggedIn.setValue(new IsLoggedInUseCase(authRepository).execute());
        favorite.setValue(new IsFavoriteUseCase(placeRepository).execute(id));

        new GetWeatherUseCase(weatherRepository).execute(current.getCity(),
                new WeatherRepository.WeatherCallback() {
                    @Override
                    public void onSuccess(Weather result) {
                        weather.setValue(result);
                    }

                    @Override
                    public void onFailure(String message) {
                        error.setValue(message);
                    }
                });
    }

    // Добавляет место в избранное или убирает из него.
    public void toggleFavorite() {
        if (placeId == -1) {
            return;
        }
        boolean isFavorite = Boolean.TRUE.equals(favorite.getValue());
        if (isFavorite) {
            new RemoveFavoriteUseCase(placeRepository).execute(placeId);
        } else {
            new AddFavoriteUseCase(placeRepository).execute(placeId);
        }
        favorite.setValue(!isFavorite);
    }
}
