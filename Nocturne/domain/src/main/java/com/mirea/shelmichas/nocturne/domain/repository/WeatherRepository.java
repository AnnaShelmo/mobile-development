package com.mirea.shelmichas.nocturne.domain.repository;

import com.mirea.shelmichas.nocturne.domain.models.Weather;

public interface WeatherRepository {
    // Получение текущей погоды по названию города из внешнего сервиса
    void getWeather(String city, WeatherCallback callback);

    interface WeatherCallback {
        void onSuccess(Weather weather);

        void onFailure(String message);
    }
}
