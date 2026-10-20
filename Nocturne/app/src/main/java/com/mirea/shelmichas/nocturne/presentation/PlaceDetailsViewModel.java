package com.mirea.shelmichas.nocturne.presentation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mirea.shelmichas.nocturne.domain.models.Place;
import com.mirea.shelmichas.nocturne.domain.models.Weather;
import com.mirea.shelmichas.nocturne.domain.repository.PlaceRepository;
import com.mirea.shelmichas.nocturne.domain.repository.WeatherRepository;
import com.mirea.shelmichas.nocturne.domain.usecase.GetWeatherUseCase;

public class PlaceDetailsViewModel extends ViewModel {

    private final PlaceRepository placeRepository;
    private final WeatherRepository weatherRepository;

    private final MutableLiveData<Place> place = new MutableLiveData<>();
    private final MutableLiveData<Weather> weather = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public PlaceDetailsViewModel(PlaceRepository placeRepository,
                                 WeatherRepository weatherRepository) {
        this.placeRepository = placeRepository;
        this.weatherRepository = weatherRepository;
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

    // Загрузка места по id и погоды для его города
    public void load(int id) {
        place.setValue(placeRepository.getPlace(id));
        Place current = place.getValue();
        if (current == null) {
            error.setValue("Место не найдено");
            return;
        }
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
}
