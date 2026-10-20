package com.mirea.shelmichas.nocturne.data.repository;

import androidx.annotation.NonNull;

import com.mirea.shelmichas.nocturne.data.network.weather.GeoResponse;
import com.mirea.shelmichas.nocturne.data.network.weather.WeatherApi;
import com.mirea.shelmichas.nocturne.data.network.weather.WeatherResponse;
import com.mirea.shelmichas.nocturne.domain.models.Weather;
import com.mirea.shelmichas.nocturne.domain.repository.WeatherRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Погода берётся из внешнего сервиса Open-Meteo через Retrofit.
// Сначала ищем координаты города геокодером, затем запрашиваем текущую погоду.
public class WeatherRepositoryImpl implements WeatherRepository {

    // Геокодер и сервис прогноза Open-Meteo (разные хосты, поэтому передаём полные адреса)
    private static final String GEOCODE_URL = "https://geocoding-api.open-meteo.com/v1/search";
    private static final String FORECAST_URL =
            "https://historical-forecast-api.open-meteo.com/v1/forecast";

    private final WeatherApi weatherApi;

    public WeatherRepositoryImpl(WeatherApi weatherApi) {
        this.weatherApi = weatherApi;
    }

    //получение координат и обработка ошибок
    @Override
    public void getWeather(String city, WeatherCallback callback) {
        weatherApi.searchCity(GEOCODE_URL, city, 1, "ru", "json")
                .enqueue(new Callback<GeoResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<GeoResponse> call,
                                           @NonNull Response<GeoResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<GeoResponse.GeoResult> results = response.body().getResults();
                            if (results == null || results.isEmpty()) {
                                callback.onFailure("Город \"" + city + "\" не найден");
                                return;
                            }
                            GeoResponse.GeoResult geo = results.get(0);
                            loadWeather(geo.getLatitude(), geo.getLongitude(), callback);
                        } else {
                            callback.onFailure("Не удалось определить координаты (код " + response.code() + ")");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<GeoResponse> call, @NonNull Throwable t) {
                        callback.onFailure("Нет соединения: " + t.getMessage());
                    }
                });
    }

    // получение погоды по координатам
    private void loadWeather(double latitude, double longitude, WeatherCallback callback) {
        weatherApi.getCurrentWeather(FORECAST_URL, latitude, longitude,
                        "temperature_2m,weather_code,wind_speed_10m,relative_humidity_2m")
                .enqueue(new Callback<WeatherResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<WeatherResponse> call,
                                           @NonNull Response<WeatherResponse> response) {
                        if (response.isSuccessful() && response.body() != null
                                && response.body().getCurrent() != null) {
                            WeatherResponse.Current current = response.body().getCurrent();
                            callback.onSuccess(new Weather(
                                    current.getTemperature(),
                                    describeWeatherCode(current.getWeatherCode()),
                                    current.getWindSpeed(),
                                    current.getHumidity()));
                        } else {
                            callback.onFailure("Сервис погоды вернул ошибку (код " + response.code() + ")");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
                        callback.onFailure("Нет соединения: " + t.getMessage());
                    }
                });
    }

    // Расшифровка кодов погоды WMO (как их отдаёт Open-Meteo)
    private String describeWeatherCode(int code) {
        if (code == 0) return "Ясно";
        if (code == 1 || code == 2) return "Переменная облачность";
        if (code == 3) return "Пасмурно";
        if (code == 45 || code == 48) return "Туман";
        if (code >= 51 && code <= 57) return "Морось";
        if (code >= 61 && code <= 67) return "Дождь";
        if (code >= 71 && code <= 77) return "Снег";
        if (code >= 80 && code <= 82) return "Ливень";
        if (code >= 85 && code <= 86) return "Снегопад";
        if (code >= 95 && code <= 99) return "Гроза";
        return "Неизвестно";
    }
}
