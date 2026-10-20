package com.mirea.shelmichas.nocturne.data.network.weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

// Внешние сервисы Open-Meteo (без ключа, данные в формате json).
// Полные адреса передаются через @Url, т.к. геокодер и прогноз лежат на разных хостах.
public interface WeatherApi {

    // Поиск координат по названию города
    @GET
    Call<GeoResponse> searchCity(@Url String url,
                                 @Query("name") String name,
                                 @Query("count") int count,
                                 @Query("language") String language,
                                 @Query("format") String format);

    // Текущая погода по координатам
    @GET
    Call<WeatherResponse> getCurrentWeather(@Url String url,
                                            @Query("latitude") double latitude,
                                            @Query("longitude") double longitude,
                                            @Query("current") String current);
}
