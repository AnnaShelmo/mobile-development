package com.mirea.shelmichas.nocturne.domain.usecase;

import com.mirea.shelmichas.nocturne.domain.repository.WeatherRepository;

public class GetWeatherUseCase {
    private final WeatherRepository weatherRepository;

    public GetWeatherUseCase(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    public void execute(String city, WeatherRepository.WeatherCallback callback) {
        weatherRepository.getWeather(city, callback);
    }
}
